package ru.vsu.pavel.zilefillabackend.util;

import java.nio.file.FileSystemException;

public class NotRegularFileException
        extends FileSystemException
{
    @java.io.Serial
    private static final long serialVersionUID = -9011457427178200199L;

    /**
     * Constructs an instance of this class.
     *
     * @param   file
     *          a string identifying the file or {@code null} if not known
     */
    public NotRegularFileException(String file) {
        super(file);
    }
}
