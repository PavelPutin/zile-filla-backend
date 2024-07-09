package ru.vsu.pavel.zilefillabackend.errors;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

import java.net.URI;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static ru.vsu.pavel.zilefillabackend.util.FileSystemUtils.stringPathToUri;

public class NotDirectoryResponseException  extends ErrorResponseException {
    public NotDirectoryResponseException(HttpStatusCode status, NotDirectoryException cause) {
        super(status, cause);
        setType(URI.create("/zile-filla/not-directory"));
        setTitle("Not a directory");
        setDetail("The file isn't a directory");
        Path instance = Paths.get("/", cause.getFile());
        setInstance(stringPathToUri(cause.getFile()));
    }
}
