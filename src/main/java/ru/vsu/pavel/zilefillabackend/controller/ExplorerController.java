package ru.vsu.pavel.zilefillabackend.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vsu.pavel.zilefillabackend.dto.FileSystemObjectDto;
import ru.vsu.pavel.zilefillabackend.dto.MoveCopyDto;
import ru.vsu.pavel.zilefillabackend.dto.RenameDto;
import ru.vsu.pavel.zilefillabackend.errors.FileAccessDeniedResponseException;
import ru.vsu.pavel.zilefillabackend.service.ExplorerService;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/explorer")
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

    @DeleteMapping(value = { "/{*path}" })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable(value = "path")
            String path
    ) {
        log.debug("ExplorerController.delete({})", path);
        var actualPath = Paths.get(
                path.startsWith("/") ? path.substring(1) : path
        );
        explorerService.delete(actualPath);
    }

    @PostMapping("/{*path}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void moveOrCopy(
            @PathVariable(value = "path")
            String path,
            @RequestBody
            MoveCopyDto moveCopyDto
    ) {
        log.debug("ExplorerController.moveOrCopy({}, {}, {})", path, moveCopyDto.actionType(), moveCopyDto.target());
        var actualPath = Paths.get(
                path.startsWith("/") ? path.substring(1) : path
        );
        var target = Paths.get(
                moveCopyDto.target().startsWith("/") ? moveCopyDto.target().substring(1) : moveCopyDto.target()
        );
        switch (moveCopyDto.actionType()) {
            case COPY -> explorerService.copy(actualPath, target);
            case MOVE -> explorerService.move(actualPath, target);
        }
    }
}
