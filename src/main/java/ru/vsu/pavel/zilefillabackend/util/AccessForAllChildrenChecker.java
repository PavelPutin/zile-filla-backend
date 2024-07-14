package ru.vsu.pavel.zilefillabackend.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;

import static java.nio.file.FileVisitResult.TERMINATE;

@Getter
@Slf4j
public class AccessForAllChildrenChecker extends SimpleFileVisitor<Path> {
    private boolean result = true;

    @Override
    public FileVisitResult visitFileFailed(Path file,
                                           IOException exc) {
        log.warn("'{file}' Deletion check failed", exc);
        result = false;
        return TERMINATE;
    }
}
