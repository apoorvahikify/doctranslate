package com.example.translator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TranslatorApplication {

    public static void main(String[] args) {
        // Set DJL environment properties for reliable CPU execution and debug logging
        System.setProperty("ai.djl.logging.level", "info");
        System.setProperty("ai.djl.default_engine", "PyTorch");

        SpringApplication.run(TranslatorApplication.class, args);
    }
}