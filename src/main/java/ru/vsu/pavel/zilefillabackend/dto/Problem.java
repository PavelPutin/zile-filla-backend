package ru.vsu.pavel.zilefillabackend.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.net.URI;

@Builder
@Getter
public class Problem<T> {
    private URI type;
    private String title;
    private HttpStatus status;
    private T detail;
    private URI instance;

    private Problem() {}
}
