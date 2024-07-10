package ru.vsu.pavel.zilefillabackend.errors;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

import java.net.URI;
import java.nio.file.NoSuchFileException;

import static ru.vsu.pavel.zilefillabackend.util.FileSystemUtils.stringPathToUri;

public class NoSuchFileResponseException extends ErrorResponseException {
    public NoSuchFileResponseException(HttpStatusCode status, NoSuchFileException cause) {
        super(status, cause);
        setType(URI.create("/zile-filla/no-such-file"));
        setTitle("No such file");
        setDetail("File does not exist");
        setInstance(stringPathToUri(cause.getFile()));
    }
}
