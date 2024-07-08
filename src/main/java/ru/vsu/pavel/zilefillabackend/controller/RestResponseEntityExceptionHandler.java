package ru.vsu.pavel.zilefillabackend.controller;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
//
//    @ExceptionHandler(NotRegularFileException.class)
//    public ResponseEntity<ProblemDetail> badRequestsHandling(NotRegularFileException ex) {
//        log.warn(ex.getMessage(), ex);
//        var problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
//        problem.setType(URI.create("/zile-filla/not-regular-file"));
//        problem.setTitle("Not regular file");
//        problem.setDetail("The file isn't a regular file (maybe, it's a directory or symbolic link)");
//        problem.setInstance(URI.create(ex.getFile()));
////        var problem = Problem.<String>builder()
////                .type(URI.create("/zile-filla/not-regular-file"))
////                .title("Not regular file")
////                .status(HttpStatus.BAD_REQUEST)
////                .detail("The file isn't a regular file (maybe, it's a directory or symbolic link)")
////                .instance(URI.create(ex.getFile()))
////                .build();
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
//    }

//    @ExceptionHandler(InvalidPathException.class)
//    public ResponseEntity<ErrorDto> badRequestsHandling(InvalidPathException ex) {
//        log.warn(ex.getMessage(), ex);
//        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
//    }
//
//    @ExceptionHandler({NotDirectoryException.class})
//    public ResponseEntity<ErrorDto> badRequestsHandling(NotDirectoryException ex) {
//        log.warn(ex.getMessage(), ex);
//        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
//    }
//
//    @ExceptionHandler(NoSuchFileException.class)
//    public ResponseEntity<ErrorDto> fileNotFoundHandling(NoSuchFileException ex) {
//        log.warn(ex.getMessage(), ex);
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(ex.getMessage()));
//    }
}
