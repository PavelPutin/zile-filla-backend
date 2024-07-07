package ru.vsu.pavel.zilefillabackend.util;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static ru.vsu.pavel.zilefillabackend.util.FileSystemUtils.*;

class FileSystemUtilsTest {
    @Test
    public void givenPathAndAbsoluteRoot_thenReturnResolvedPath() {
        Path path = Paths.get("ets\\photos.txt");
        Path root = Paths.get("C:\\custom-root");
        Path actual = getPathForRoot(path, root);
        Path expected = Paths.get("C:\\custom-root\\ets\\photos.txt");
        assertEquals(expected, actual);
    }

    @Test
    public void givenPath_whenRootIsNotAbsolute_thenThrow() {
        Path path = Paths.get("ets\\photos.txt");
        Path root = Paths.get("custom-root");
        assertThrows(IllegalArgumentException.class, () -> getPathForRoot(path, root));
    }
}