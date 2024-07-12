package ru.vsu.pavel.zilefillabackend.errors;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

import java.net.URI;

import static ru.vsu.pavel.zilefillabackend.util.FileSystemUtils.stringPathToUri;

public class DirectoryNotEmptyResponseException extends ErrorResponseException {
    public DirectoryNotEmptyResponseException(HttpStatusCode status, String file) {
        super(status);
        setType(URI.create("/zile-filla/directory-not-empty"));
        setTitle("Directory is not empty");
        setDetail("You can't move or copy source file to destination, because source file is an not empty directory");
        setInstance(stringPathToUri(file));
    }
}
