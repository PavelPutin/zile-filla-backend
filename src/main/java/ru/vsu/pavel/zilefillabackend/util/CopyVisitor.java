package ru.vsu.pavel.zilefillabackend.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;


@Slf4j
public class CopyVisitor extends SimpleFileVisitor<Path> {
    private final Path fromPath;
    private final Path toPath;

    private final StandardCopyOption copyOption =
            StandardCopyOption.REPLACE_EXISTING;

    public CopyVisitor(Path fromPath, Path toPath)
    {
        this.fromPath = fromPath;
        this.toPath = toPath;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir,
                                             BasicFileAttributes attrs)
            throws IOException
    {
        log.debug("dir = {}", dir);
        log.debug("pre visit: fromPath = {}", fromPath);
        log.debug("pre visit: toPath = {}", toPath);
        log.debug("pre visit: fromPath.relativize(dir) = {}", fromPath.relativize(dir));
        log.debug("pre visit: toPath.resolve(fromPath.relativize(dir)) = {}", toPath.resolve(fromPath.relativize(dir)));

        Path targetPath = toPath.resolve(fromPath.relativize(dir));
        if (!Files.exists(targetPath))
            Files.createDirectory(targetPath);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
            throws IOException
    {
        log.debug("file = {}", file);
        log.debug("file visit: fromPath = {}", fromPath);
        log.debug("file visit: toPath = {}", toPath);
        log.debug("file visit: fromPath.relativize(file) = {}", fromPath.relativize(file));
        log.debug("file visit: toPath.resolve(fromPath.relativize(file)) = {}", toPath.resolve(fromPath.relativize(file)));

        Files.copy(file, toPath.resolve(fromPath.relativize(file)),
                copyOption);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException ioe)
    {
        log.warn(ioe.getMessage(), ioe);
        return FileVisitResult.CONTINUE;
    }
}
