package ru.vsu.pavel.zilefillabackend.errors;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CantCheckFileTypeResponseException extends ErrorResponseException {
    public CantCheckFileTypeResponseException(HttpStatusCode status, String file) {
        super(status);
        setType(URI.create("/zile-filla/cant-check-file-type/"));
        setTitle("Can't check file type");
        setDetail("IOException occurred while checking file type");
        Path instance = Paths.get("/", file);
        setInstance(instance.toUri());
    }
}
