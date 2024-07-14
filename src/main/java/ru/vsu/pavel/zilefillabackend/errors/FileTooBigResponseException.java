package ru.vsu.pavel.zilefillabackend.errors;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

import java.net.URI;

import static ru.vsu.pavel.zilefillabackend.util.FileSystemUtils.stringPathToUri;

public class FileTooBigResponseException extends ErrorResponseException {
    public FileTooBigResponseException(HttpStatusCode status, String file) {
        super(status);
        setType(URI.create("/zile-filla/file-too-big"));
        setTitle("File too big");
        setDetail("File size is greater then 100 mb - it's greater then allowed size");
        setInstance(stringPathToUri(file));
    }
}
