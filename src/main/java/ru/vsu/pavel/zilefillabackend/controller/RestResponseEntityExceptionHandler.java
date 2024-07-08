package ru.vsu.pavel.zilefillabackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.vsu.pavel.zilefillabackend.errors.InvalidPathResponseException;

import java.nio.file.InvalidPathException;
import java.util.Optional;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { InvalidPathException.class })
    public ResponseEntity<Object> handleInvalidPathException(final InvalidPathException ex, final WebRequest request) {
        return ResponseEntity.of(Optional.of(new InvalidPathResponseException(HttpStatus.BAD_REQUEST, ex)));
    }
}
