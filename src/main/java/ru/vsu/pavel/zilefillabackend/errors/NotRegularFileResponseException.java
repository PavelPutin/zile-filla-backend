package ru.vsu.pavel.zilefillabackend.errors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;
import ru.vsu.pavel.zilefillabackend.util.NotRegularFileException;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class NotRegularFileResponseException extends ErrorResponseException {
    public NotRegularFileResponseException(HttpStatusCode status, NotRegularFileException cause) {
        super(status, cause);
        setType(URI.create("/zile-filla/not-regular-file"));
        setTitle("Not regular file");
        setDetail("The file isn't a regular file (maybe, it's a directory or symbolic link)");
        Path instance = Paths.get("/", cause.getFile());
        setInstance(instance.toUri());
    }
}
