package ru.vsu.pavel.zilefillabackend.util;

import java.nio.file.Path;

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
}
