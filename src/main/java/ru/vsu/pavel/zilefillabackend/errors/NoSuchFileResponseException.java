package ru.vsu.pavel.zilefillabackend.errors;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

import java.net.URI;
import java.nio.file.NoSuchFileException;

public class NoSuchFileResponseException extends ErrorResponseException {
    public NoSuchFileResponseException(HttpStatusCode status, NoSuchFileException cause) {
        super(status, cause);
        setType(URI.create("/zile-filla/no-such-file"));
        setTitle("No such file");
        setDetail("File does not exist");
        setInstance(URI.create("/" + cause.getFile().replaceAll("\\\\", "/")));
    }
}
