package ru.vsu.pavel.zilefillabackend.errors;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

import java.net.URI;

import static ru.vsu.pavel.zilefillabackend.util.FileSystemUtils.stringPathToUri;

public class IOExceptionResponseException extends ErrorResponseException {
    public IOExceptionResponseException(HttpStatusCode status, String file) {
        super(status);
        setType(URI.create("/zile-filla/internal-io-exception"));
        setTitle("Can't check file type");
        setDetail("Internal IOException occurred");
        setInstance(stringPathToUri(file));
    }
}
