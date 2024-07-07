package ru.vsu.pavel.zilefillabackend.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileSystemUtils {
    /**
     * Получает путь в поддереве файловой системы, с корнем в директории root
     * @param path путь внутри поддерева, начинающегося в root
     * @param root корневая директория поддерева
     * @return путь в поддереве
     * @throws IllegalArgumentException если корень не является абсолютным путём
     */
    public static Path getPathForRoot(final Path path, final Path root) {
        if (!root.isAbsolute()) {
            throw new IllegalArgumentException("Root must be absolute");
        }
        var normalized = path.normalize();
        return root.resolve(normalized);
    }

    /**
     * Вычисляет размер папки с учётом всех её потомков
     * @param path директория, размер которой вычисляется
     * @return размер папки в байтах
     */
    public static long getDirectorySizeBytes(final Path path) {
        var calculator = new DirectorySizeCalculator();
        try {
            Files.walkFileTree(path, calculator);
            return calculator.getSize();
        } catch (final SecurityException e) {
            log.warn("Deny access to '{}'", path, e);
            return 0;
        } catch (final IOException e) {
            log.warn("Can't get size of '{}'", path, e);
            return 0;
        }
    }
}
