package ru.vsu.pavel.zilefillabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.vsu.pavel.zilefillabackend.controller.FileReaderController;

@SpringBootApplication
public class ZileFillaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZileFillaBackendApplication.class, args);
    }
}
