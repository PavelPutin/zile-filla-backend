package ru.vsu.pavel.zilefillabackend.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;

@Getter
@Slf4j
public class DirectorySizeCalculator extends SimpleFileVisitor<Path> {
    private long size = 0;
    private boolean accurate = true;

    @Override
    public FileVisitResult visitFile(Path file,
                                     BasicFileAttributes attr) {
        size += attr.size();
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file,
                                           IOException exc) {
        log.error(exc.getMessage(), exc);
        accurate = false;
        return CONTINUE;
    }
}
