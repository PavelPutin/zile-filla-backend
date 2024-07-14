package ru.vsu.pavel.zilefillabackend.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.vsu.pavel.zilefillabackend.errors.*;
import ru.vsu.pavel.zilefillabackend.util.FileSystemUtils;
import ru.vsu.pavel.zilefillabackend.util.NotRegularFileException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;

@Service
@Slf4j
public class FileSystemAccessService extends FileSystemUtils {

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

    public void exists(final Path inSubTree, final Path original) {
        if (!Files.exists(inSubTree)) {
            log.warn("'{}' does not exist", inSubTree);
            throw new NoSuchFileResponseException(HttpStatus.NOT_FOUND, new NoSuchFileException(original.toString()));
        }
    }

    public void notExists(final Path inSubTree, final Path original) {
        if (Files.exists(inSubTree)) {
            log.warn("'{}' already exists", inSubTree);
            throw new FileAlreadyExistsResponseException(HttpStatus.CONFLICT, original.toString());
        }
    }

    public void isDirectory(final Path inSubTree, final Path original) {
        if (!Files.isDirectory(inSubTree)) {
            log.warn("Try get not directory '{}'", inSubTree);
            throw new NotDirectoryResponseException(HttpStatus.BAD_REQUEST, new NotDirectoryException(original.toString()));
        }
    }

    public void isRegularFile(final Path inSubTree, final Path original) {
        if (!Files.isRegularFile(inSubTree)) {
            log.warn("Try get not file '{}'", original);
            throw new NotRegularFileResponseException(
                    HttpStatus.BAD_REQUEST,
                    new NotRegularFileException(original.toString()));
        }
    }

    public void isTextFile(final Path inSubTree, final Path original) throws IOException {
        var contentType = Files.probeContentType(inSubTree);
        if (contentType == null || !Files.probeContentType(inSubTree).startsWith("text/")) {
            log.warn("'{}' is not a text file", original);
            throw new NotTextFileResponseException(HttpStatus.BAD_REQUEST, original.toString());
        }
    }

    public void isSizeLessThenMax(final Path inSubTree, final Path original) throws IOException {
        final long maxFileSize = 100 * 1024 * 1024; // 100 MiB
        if (Files.size(inSubTree) >= maxFileSize) {
            log.warn("'{}' size is greater then {} bytes", original, maxFileSize);
            throw new FileTooBigResponseException(HttpStatus.BAD_REQUEST, original.toString());
        }
    }
}
