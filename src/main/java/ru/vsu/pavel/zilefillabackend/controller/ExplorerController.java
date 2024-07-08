package ru.vsu.pavel.zilefillabackend.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vsu.pavel.zilefillabackend.dto.ErrorDto;
import ru.vsu.pavel.zilefillabackend.dto.FileSystemObjectDto;
import ru.vsu.pavel.zilefillabackend.service.ExplorerService;

import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/explorer")
@AllArgsConstructor
@Slf4j
public class ExplorerController {

    private final ExplorerService explorerService;

    @GetMapping(value = { "", "/", "/{*path}" })
    public ResponseEntity<List<FileSystemObjectDto>> changeDirectory(@PathVariable(value = "path") Optional<String> path) throws NotDirectoryException, NoSuchFileException {
        log.debug("ExplorerController.changeDirectory({})", path);

        var actualPath = path.orElse("");
        if (!actualPath.isEmpty()) {
            actualPath = actualPath.substring(1);
        }
        return ResponseEntity.ok(explorerService.changeDirectory(Paths.get(actualPath)));
    }

    @ExceptionHandler({InvalidPathException.class, NotDirectoryException.class})
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
