package ru.vsu.pavel.zilefillabackend.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;

import java.nio.file.attribute.BasicFileAttributes;

@Slf4j
public class MoveVisitor extends SimpleFileVisitor<Path>
{
    private final Path fromPath, toPath;

    public MoveVisitor(Path srcPath, Path toPath)
    {
        this.fromPath = srcPath;
        this.toPath = toPath;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir,
                                              IOException ioe)
            throws IOException
    {
        log.debug("dir = {}", dir);
        log.debug("post visit: fromPath = {}", fromPath);
        log.debug("post visit: toPath = {}", toPath);
        log.debug("post visit: fromPath.relativize(dir) = {}", fromPath.relativize(dir));
        log.debug("post visit: toPath.resolve(fromPath.relativize(dir)) = {}", toPath.resolve(fromPath.relativize(dir)));
        if (ioe == null)
            Files.delete(dir);
        else
            throw ioe;
        return FileVisitResult.CONTINUE;
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
        Files.copy(dir, targetPath, StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.COPY_ATTRIBUTES);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file,
                                     BasicFileAttributes attr)
            throws IOException
    {
        log.debug("file = {}", file);
        log.debug("file visit: fromPath = {}", fromPath);
        log.debug("file visit: toPath = {}", toPath);
        log.debug("file visit: fromPath.relativize(file) = {}", fromPath.relativize(file));
        log.debug("file visit: toPath.resolve(fromPath.relativize(file)) = {}", toPath.resolve(fromPath.relativize(file)));
        Path targetPath = toPath.resolve(fromPath.relativize(file));
        Files.move(file, targetPath,
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE);

        return FileVisitResult.CONTINUE;
    }
}
