package com.example.zestindiaassignment;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Student Management API", version = "1.0.0"))
public class ZestIndiaAssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZestIndiaAssignmentApplication.class, args);
    }

}
