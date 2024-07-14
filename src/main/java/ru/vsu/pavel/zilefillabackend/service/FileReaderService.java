package ru.vsu.pavel.zilefillabackend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.vsu.pavel.zilefillabackend.dto.TextFileContentDto;
import ru.vsu.pavel.zilefillabackend.errors.*;
import ru.vsu.pavel.zilefillabackend.util.NotRegularFileException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class FileReaderService {

    private final FileSystemAccessService fileSystemAccessService;

    public TextFileContentDto getTextFileContent(Path path) {
        log.debug("FileReaderService.getTextFileContent({})", path);
        var pathInSubTree = fileSystemAccessService.getPathInSubtree(path);
        log.debug("Get text file content path in subtree: {}", pathInSubTree);

        fileSystemAccessService.exists(pathInSubTree, path);
        fileSystemAccessService.isRegularFile(pathInSubTree, path);

        try {
            fileSystemAccessService.isTextFile(pathInSubTree, path);
            fileSystemAccessService.isSizeLessThenMax(pathInSubTree, path);
        } catch (IOException e) {
            log.warn("Can't check file type '{}' because of IOException", path, e);
            throw new IOExceptionResponseException(HttpStatus.INTERNAL_SERVER_ERROR, path.toString());
        } catch (SecurityException e) {
            log.warn("Can't check file type '{}' because of SecurityException", path, e);
            throw new FileAccessDeniedResponseException(HttpStatus.FORBIDDEN, path.toString());
        }

        try (BufferedReader reader = Files.newBufferedReader(pathInSubTree);
             Stream<String> lines = reader.lines()) {
            return new TextFileContentDto(lines.collect(Collectors.joining("\n")));
        } catch (AccessDeniedException e) {
            log.warn("Can't read file '{}' because of SecurityException", path, e);
            throw new FileAccessDeniedResponseException(HttpStatus.FORBIDDEN, path.toString());
        } catch (IOException e) {
            log.warn("Can't read file '{}'", path, e);
            throw new IOExceptionResponseException(HttpStatus.INTERNAL_SERVER_ERROR, path.toString());
        }
    }
}
