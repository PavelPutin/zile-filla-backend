package ru.vsu.pavel.zilefillabackend.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vsu.pavel.zilefillabackend.dto.TextFileContent;
import ru.vsu.pavel.zilefillabackend.service.FileReaderService;
import ru.vsu.pavel.zilefillabackend.util.NotRegularFileException;

import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

@RestController
@RequestMapping("/view")
@AllArgsConstructor
@Slf4j
public class FileReaderController {
    private final FileReaderService fileReaderService;

    @GetMapping("/{*path}")
    public ResponseEntity<TextFileContent> getFileContent(
            @PathVariable("path")
            String path
    ) throws NoSuchFileException {
        log.debug("FileReaderController.getFileContent({})", path);
        if (!path.isBlank()) {
            path = path.substring(1);
        }
        var actualPath = Paths.get(path);
        return ResponseEntity.ok(fileReaderService.getTextFileContent(actualPath));
    }
}
