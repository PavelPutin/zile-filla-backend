package ru.vsu.pavel.zilefillabackend.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;

@Getter
@Slf4j
public class DirectoryDeleter extends SimpleFileVisitor<Path> {

    @Override
    public FileVisitResult visitFile(Path file,
                                     BasicFileAttributes attr) throws IOException {
        log.debug("Deleting file: {}", file);
        Files.deleteIfExists(file);
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path file,
                                              IOException exc) throws IOException {
        log.debug("(Post visit) Deleting file: {}", file);
        Files.deleteIfExists(file);
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file,
                                           IOException exc) {
        log.warn("'{file}' Deletion failed", exc);
        return TERMINATE;
    }
}
