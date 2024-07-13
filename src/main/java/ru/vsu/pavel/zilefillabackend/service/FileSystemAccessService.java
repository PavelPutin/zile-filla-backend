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
            var e = new IllegalArgumentException(root + " is not absolute");
            log.error("Root path is not absolute: {}", root, e);
            throw e;
        }
        if (!Files.exists(rootPath)) {
            var e = new IllegalArgumentException(root + " does not exist");
            log.error("Root path does not exist: {}", root, e);
            throw e;
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


    /**
     * Проверяет, совпадает ли переданный путь с корнем публикуемого поддерева
     * @param path проверяемый путь
     * @return true если путь совпадает с корнем поддерева, иначе false
     */
    public boolean isRoot(final Path path) {
        return path.equals(rootPath);
    }
}
