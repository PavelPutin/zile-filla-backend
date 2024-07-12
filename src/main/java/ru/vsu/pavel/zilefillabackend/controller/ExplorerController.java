package ru.vsu.pavel.zilefillabackend.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vsu.pavel.zilefillabackend.dto.FileSystemObjectDto;
import ru.vsu.pavel.zilefillabackend.dto.RenameDto;
import ru.vsu.pavel.zilefillabackend.errors.FileAccessDeniedResponseException;
import ru.vsu.pavel.zilefillabackend.errors.NoSuchFileResponseException;
import ru.vsu.pavel.zilefillabackend.service.ExplorerService;

import java.io.FileNotFoundException;
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
    ) {
        log.debug("ExplorerController.changeDirectory({})", path);

        var actualPath = path.orElse("");
        if (!actualPath.isEmpty()) {
            actualPath = actualPath.substring(1);
        }
        return ResponseEntity.ok(explorerService.changeDirectory(Paths.get(actualPath)));
    }

    @PutMapping(value = { "/{*path}" })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void rename(
            @PathVariable(value = "path")
            String path,
            @RequestBody
            RenameDto renameDto
    ) {
        log.debug("ExplorerController.rename({})", path);
        if (path.isBlank() || path.equals("/")) {
            throw new FileAccessDeniedResponseException(HttpStatus.FORBIDDEN, "");
        }
        var actualPath = Paths.get(
                path.startsWith("/") ? path.substring(1) : path
        );
        explorerService.rename(actualPath, renameDto);
    }
}
