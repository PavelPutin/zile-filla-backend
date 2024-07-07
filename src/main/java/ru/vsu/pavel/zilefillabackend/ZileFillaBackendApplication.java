package ru.vsu.pavel.zilefillabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationAotProcessor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
public class ZileFillaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZileFillaBackendApplication.class, args);
    }
}
