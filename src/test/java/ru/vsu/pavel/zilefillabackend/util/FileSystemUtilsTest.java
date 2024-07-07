package ru.vsu.pavel.zilefillabackend.util;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static ru.vsu.pavel.zilefillabackend.util.FileSystemUtils.*;

class FileSystemUtilsTest {
    @Test
    public void givenPathAndAbsoluteRoot_thenReturnResolvedPath() {
        try (FileSystem fileSystem = Jimfs.newFileSystem(Configuration.windows())) {
            Path path = fileSystem.getPath("ets\\photos.txt");
            Path root = fileSystem.getPath("C:\\custom-root");
            Path actual = getPathForRoot(path, root);
            Path expected = fileSystem.getPath("C:\\custom-root\\ets\\photos.txt");
            assertEquals(expected, actual);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void givenPath_whenRootIsNotAbsolute_thenThrow() {
        try (FileSystem fileSystem = Jimfs.newFileSystem(Configuration.windows())) {
            Path path = fileSystem.getPath("ets\\photos.txt");
            Path root = fileSystem.getPath("custom-root");
            assertThrows(IllegalArgumentException.class, () -> getPathForRoot(path, root));
        } catch (IOException e) {
            fail();
        }
    }
}