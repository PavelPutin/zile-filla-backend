package ru.vsu.pavel.zilefillabackend.errors;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

import java.net.URI;

import static ru.vsu.pavel.zilefillabackend.util.FileSystemUtils.stringPathToUri;

public class FileAccessDeniedResponseException extends ErrorResponseException {
    public FileAccessDeniedResponseException(HttpStatusCode status, String file) {
        super(status);
        setType(URI.create("/zile-filla/file-access-denied/"));
        setTitle("File access denied");
        setDetail("You do not have permission to access this file");
        setInstance(stringPathToUri(file));
    }
}
