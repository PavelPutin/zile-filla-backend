package ru.vsu.pavel.zilefillabackend.errors;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

import java.net.URI;

import static ru.vsu.pavel.zilefillabackend.util.FileSystemUtils.stringPathToUri;

public class FileAlreadyExistsResponseException extends ErrorResponseException {
    public FileAlreadyExistsResponseException(HttpStatusCode status, String file) {
        super(status);
        setType(URI.create("/zile-filla/file-already-exists"));
        setTitle("File already exists");
        setDetail("You can't move or copy source file to destination, because destination file already exists");
        setInstance(stringPathToUri(file));
    }
}
