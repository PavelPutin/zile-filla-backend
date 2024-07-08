package ru.vsu.pavel.zilefillabackend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vsu.pavel.zilefillabackend.dto.TextFileContent;
import ru.vsu.pavel.zilefillabackend.util.NotRegularFileException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class FileReaderService {

    private final FileSystemAccessService fileSystemAccessService;

    public TextFileContent getTextFileContent(Path path) throws NoSuchFileException, NotRegularFileException {
        log.debug("FileReaderService.getTextFileContent({})", path);
        // TODO: убрать дублирование кода
        var pathInSubTree = fileSystemAccessService.getPathInSubtree(path);
        log.debug("Get text file content path in subtree: {}", pathInSubTree);

        // TODO: убрать дублирование кода
        if (!Files.exists(pathInSubTree)) {
            log.warn("'{}' does not exist", path);
            throw new NoSuchFileException(path.toString());
        }

        if (!Files.isRegularFile(pathInSubTree)) {
            log.warn("Try get not file '{}'", path);
            throw new NotRegularFileException(path.toString());
        }

        try {
            if (!Files.probeContentType(pathInSubTree).startsWith("text/")) {
                log.warn("'{}' is not a text file", path);
                throw new IllegalArgumentException("Not a text file");
            }
        } catch (IOException e) {
            log.warn("Could not check file type '{}'", path, e);
            throw new RuntimeException("Can't check file type", e);
        }

        try (BufferedReader reader = Files.newBufferedReader(pathInSubTree);
             Stream<String> lines = reader.lines()) {
            return new TextFileContent(lines.collect(Collectors.joining("\n")));
        } catch (IOException e) {
            log.warn("Could not read file '{}'", path, e);
            throw new RuntimeException("Can't read file", e);
        }
    }
}
