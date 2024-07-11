package ru.vsu.pavel.zilefillabackend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.vsu.pavel.zilefillabackend.dto.FileMetadata;
import ru.vsu.pavel.zilefillabackend.dto.FileSystemObjectDto;
import ru.vsu.pavel.zilefillabackend.dto.FileSystemObjectType;
import ru.vsu.pavel.zilefillabackend.errors.FileAccessDeniedResponseException;
import ru.vsu.pavel.zilefillabackend.errors.IOExceptionResponseException;
import ru.vsu.pavel.zilefillabackend.errors.NoSuchFileResponseException;
import ru.vsu.pavel.zilefillabackend.errors.NotDirectoryResponseException;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static ru.vsu.pavel.zilefillabackend.util.FileSystemUtils.getDirectorySizeBytes;

@Service
@AllArgsConstructor
@Slf4j
public class ExplorerService {

    private final FileSystemAccessService fileSystemAccessService;

    public List<FileSystemObjectDto> changeDirectory(Path path) {
        log.debug("FileSystemService.changeDirectory({})", path);
        // TODO: убрать дублирование кода
        var pathInSubTree = fileSystemAccessService.getPathInSubtree(path);
        log.debug("Change directory path in subtree: {}", pathInSubTree);

        // TODO: убрать дублирование кода
        if (!Files.exists(pathInSubTree)) {
            log.warn("'{}' does not exist", path);
            throw new NoSuchFileResponseException(HttpStatus.NOT_FOUND, new NoSuchFileException(path.toString()));
        }

        if (!Files.isDirectory(pathInSubTree)) {
            log.warn("Try get not directory '{}'", path);
            throw new NotDirectoryResponseException(HttpStatus.BAD_REQUEST, new NotDirectoryException(path.toString()));
        }

        var result = new ArrayList<FileSystemObjectDto>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(pathInSubTree)) {
            for (Path file : stream) {
                log.debug("File: {}", file);
                var attr = Files.readAttributes(file, BasicFileAttributes.class);
                var sizeData = getDirectorySizeBytes(file);
                var metadata = new FileMetadata(
                        sizeData.value(),
                        sizeData.accurate(),
                        attr.creationTime().toInstant(),
                        attr.lastAccessTime().toInstant(),
                        attr.lastModifiedTime().toInstant()
                );
                result.add(new FileSystemObjectDto(
                        attr.isDirectory() ? FileSystemObjectType.DIRECTORY.dtoValue : FileSystemObjectType.FILE.dtoValue,
                        file.getFileName().toString(),
                        metadata
                ));
            }
        } catch (AccessDeniedException e) {
            log.warn(e.getMessage(), e);
            throw new FileAccessDeniedResponseException(HttpStatus.FORBIDDEN, path.toString());
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            throw new IOExceptionResponseException(HttpStatus.INTERNAL_SERVER_ERROR, path.toString());
        }
        log.debug("Change directory result: {}", result);
        result.sort((a, b) -> {

            if (a.type().equals(FileSystemObjectType.DIRECTORY.dtoValue) && b.type().equals(FileSystemObjectType.FILE.dtoValue)) {
                return -1;
            }

            if (a.type().equals(FileSystemObjectType.FILE.dtoValue) && b.type().equals(FileSystemObjectType.DIRECTORY.dtoValue)) {
                return 1;
            }

            return a.name().compareTo(b.name());
        });
        return result;
    }
}
