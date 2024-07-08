package ru.vsu.pavel.zilefillabackend.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vsu.pavel.zilefillabackend.dto.ErrorDto;
import ru.vsu.pavel.zilefillabackend.dto.TextFileContent;
import ru.vsu.pavel.zilefillabackend.service.FileReaderService;
import ru.vsu.pavel.zilefillabackend.util.NotRegularFileException;

import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

@RestController
@RequestMapping("/view")
@AllArgsConstructor
@Slf4j
public class FileReaderController {
    private final FileReaderService fileReaderService;

    @GetMapping("/{*path}")
    public ResponseEntity<TextFileContent> getFileContent(@PathVariable("path") String path) throws NoSuchFileException, NotRegularFileException {
        log.debug("FileReaderController.getFileContent({})", path);
        if (!path.isBlank()) {
            path = path.substring(1);
        }
        var actualPath = Paths.get(path);
        return ResponseEntity.ok(fileReaderService.getTextFileContent(actualPath));
    }

    @ExceptionHandler({InvalidPathException.class, NotRegularFileException.class})
    public ResponseEntity<ErrorDto> badRequestsHandling(Exception ex) {
        log.warn(ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler({NoSuchFileException.class})
    public ResponseEntity<ErrorDto> fileNotFoundHandling(Exception ex) {
        log.warn(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(ex.getMessage()));
    }
}
