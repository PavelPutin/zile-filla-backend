package ru.vsu.pavel.zilefillabackend.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vsu.pavel.zilefillabackend.dto.FileSystemObjectDto;
import ru.vsu.pavel.zilefillabackend.service.ExplorerService;

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
    public ResponseEntity<List<FileSystemObjectDto>> changeDirectory(
            @PathVariable(value = "path")
            Optional<String> path
    ) throws NotDirectoryException {
        log.debug("ExplorerController.changeDirectory({})", path);

        var actualPath = path.orElse("");
        if (!actualPath.isEmpty()) {
            actualPath = actualPath.substring(1);
        }
        return ResponseEntity.ok(explorerService.changeDirectory(Paths.get(actualPath)));
    }
}
