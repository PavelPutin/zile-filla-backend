package ru.vsu.pavel.zilefillabackend.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.vsu.pavel.zilefillabackend.dto.FileSystemObjectDto;
import ru.vsu.pavel.zilefillabackend.service.FileSystemService;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/explorer")
@AllArgsConstructor
@Slf4j
public class ExplorerController {

    private final FileSystemService fileSystemService;

    @GetMapping
    public ResponseEntity<List<FileSystemObjectDto>> getRootChildren() {
        return changeDirectory("");
    }

    @GetMapping("/{path}")
    public ResponseEntity<List<FileSystemObjectDto>> changeDirectory(@PathVariable("path") String path) {
        try {
            var actualPath = Paths.get(path);
            return ResponseEntity.ok(fileSystemService.changeDirectory(actualPath));
        } catch (InvalidPathException e) {
            log.warn(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
