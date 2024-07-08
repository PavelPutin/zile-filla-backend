package ru.vsu.pavel.zilefillabackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.vsu.pavel.zilefillabackend.dto.ErrorDto;

import ru.vsu.pavel.zilefillabackend.util.NotRegularFileException;

import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;

@RestControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NotRegularFileException.class)
    public ResponseEntity<ErrorDto> badRequestsHandling(Exception ex) {
        log.warn(ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(NoSuchFileException.class)
    public ResponseEntity<ErrorDto> fileNotFoundHandling(Exception ex) {
        log.warn(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(InvalidPathException.class)
    public ResponseEntity<ErrorDto> badRequestsHandling(InvalidPathException ex) {
        log.warn(ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler({NotDirectoryException.class})
    public ResponseEntity<ErrorDto> badRequestsHandling(NotDirectoryException ex) {
        log.warn(ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(NoSuchFileException.class)
    public ResponseEntity<ErrorDto> fileNotFoundHandling(NoSuchFileException ex) {
        log.warn(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(ex.getMessage()));
    }
}
