package com.example.simple_template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.example.simple_template.db.dao")
public class SimpleTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleTemplateApplication.class, args);
    }

}
