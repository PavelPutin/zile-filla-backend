package ru.vsu.pavel.zilefillabackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.vsu.pavel.zilefillabackend.dto.FileMetadata;
import ru.vsu.pavel.zilefillabackend.dto.FileSystemObjectDto;
import ru.vsu.pavel.zilefillabackend.dto.FileSystemObjectType;
import ru.vsu.pavel.zilefillabackend.util.FileSystemUtils;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static ru.vsu.pavel.zilefillabackend.util.FileSystemUtils.*;

@Service
@Slf4j
public class FileSystemService {
    @Value("${zilefilla.filesystem.root}")
    public String root;

    public List<FileSystemObjectDto> changeDirectory(Path path) {
        log.debug("Change directory: {}", path);
        var rootPath = Path.of(root);
        var pathInSubTree = getPathForRoot(path, rootPath);
        log.debug("Change directory path: {}", pathInSubTree);

        var result = new ArrayList<FileSystemObjectDto>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(pathInSubTree)) {
            for (Path file: stream) {
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
