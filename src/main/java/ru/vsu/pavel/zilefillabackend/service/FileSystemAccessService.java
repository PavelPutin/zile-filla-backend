package ru.vsu.pavel.zilefillabackend.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

import static ru.vsu.pavel.zilefillabackend.util.FileSystemUtils.getPathForRoot;

@Service
@Slf4j
public class FileSystemAccessService {

    @Value("${zilefilla.filesystem.root}")
    private String root;
    private Path rootPath;

    @PostConstruct
    public void init() {
        log.info("Init FileSystemAccessService with root '{}'", root);
        rootPath = Path.of(root);
        if (!rootPath.isAbsolute()) {
            throw new IllegalArgumentException(root + " is not absolute");
        }
        if (!Files.exists(rootPath)) {
            throw new IllegalArgumentException(root + " does not exist");
        }
    }


    /**
     * Добавляет к переданному пути заданный в конфигурации корень поддерева
     * @param path путь в поддереве
     * @return путь с префиксом корня поддерева
     */
    public Path getPathInSubtree(final Path path) {
        var pathInSubTree = getPathForRoot(path, rootPath);
        log.debug("Get path in subtree '{}'", pathInSubTree);

        return pathInSubTree;
    }
}
