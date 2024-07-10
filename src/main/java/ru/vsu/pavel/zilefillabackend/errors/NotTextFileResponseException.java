package ru.vsu.pavel.zilefillabackend.errors;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

import java.net.URI;

import static ru.vsu.pavel.zilefillabackend.util.FileSystemUtils.stringPathToUri;

public class NotTextFileResponseException extends ErrorResponseException {
    public NotTextFileResponseException(HttpStatusCode status, String file) {
        super(status);
        setType(URI.create("/zile-filla/not-text-file"));
        setTitle("Not text file");
        setDetail("The file isn't a text file");
        setInstance(stringPathToUri(file));
    }
}
