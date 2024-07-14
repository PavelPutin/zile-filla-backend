package ru.vsu.pavel.zilefillabackend.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
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
        log.debug("Root path: {}", root);
        if (!root.isAbsolute()) {
            throw new IllegalArgumentException("Root must be absolute");
        }
        var normalized = path.normalize();
        log.debug("Normalized path: {}", normalized);
        log.debug("Resolved path: {}", root.resolve(normalized));
        return root.resolve(normalized);
    }

    /**
     * Вычисляет размер файла или папки с учётом всех её потомков. Если не удалось получить доступ возвращает специальное значение -1
     * @param path директория, размер которой вычисляется
     * @return размер папки в байтах или -1, если не удалось получить доступ
     */
    public static DirectorySize getDirectorySizeBytes(final Path path) throws IOException {
        var calculator = new DirectorySizeCalculator();
        try {
            Files.walkFileTree(path, calculator);
            return new DirectorySize(calculator.getSize(), calculator.isAccurate());
        } catch (SecurityException e) {
            log.warn("Security exception", e);
            return new DirectorySize(-1, false);
        }
    }

    public static boolean checkAccessForDelete(final Path path) throws IOException {
        var checker = new AccessForAllChildrenChecker();
        Files.walkFileTree(path, checker);
        return checker.isResult();
    }

    public static void deleteDirectory(final Path path) throws IOException {
        var deleter = new DirectoryDeleter();
        Files.walkFileTree(path, deleter);
    }

    public record DirectorySize(long value, boolean accurate) {}

    public static URI stringPathToUri(final String path) {
        return URI.create("/" + path.replaceAll("\\\\", "/").replaceAll(" ", "%20"));
    }
}
