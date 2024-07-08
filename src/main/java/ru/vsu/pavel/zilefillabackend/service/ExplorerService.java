package ru.vsu.pavel.zilefillabackend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.vsu.pavel.zilefillabackend.dto.FileMetadata;
import ru.vsu.pavel.zilefillabackend.dto.FileSystemObjectDto;
import ru.vsu.pavel.zilefillabackend.dto.FileSystemObjectType;
import ru.vsu.pavel.zilefillabackend.errors.NoSuchFileResponseException;

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

    public List<FileSystemObjectDto> changeDirectory(Path path) throws NotDirectoryException {
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
            throw new NotDirectoryException(path.toString());
        }

        var result = new ArrayList<FileSystemObjectDto>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(pathInSubTree)) {
            for (Path file : stream) {
                log.debug("File: {}", file);
                var attr = Files.readAttributes(file, BasicFileAttributes.class);
                long size = getDirectorySizeBytes(file);
                var metadata = new FileMetadata(
                        size,
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
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.debug("Change directory result: {}", result);
        return result;
    }
}
