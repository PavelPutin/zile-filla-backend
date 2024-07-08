package ru.vsu.pavel.zilefillabackend.errors;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NotTextFileResponseException extends ErrorResponseException {
    public NotTextFileResponseException(HttpStatusCode status, String file) {
        super(status);
        setType(URI.create("/zile-filla/not-regular-file"));
        setTitle("Not regular file");
        setDetail("The file isn't a regular file (maybe, it's a directory or symbolic link)");
        Path instance = Paths.get("/", file);
        setInstance(instance.toUri());
    }
}
