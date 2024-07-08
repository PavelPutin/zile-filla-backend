package ru.vsu.pavel.zilefillabackend.errors;


import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

import java.net.URI;
import java.nio.file.InvalidPathException;

public class InvalidPathResponseException extends ErrorResponseException {
    public InvalidPathResponseException(HttpStatusCode status, InvalidPathException cause) {
        super(status, cause);
        setType(URI.create("/zile-filla/invalid-path"));
        setTitle("Not regular file");
        setDetail("The file isn't a regular file (maybe, it's a directory or symbolic link)");
        setInstance(URI.create("/explorer"));
    }
}
