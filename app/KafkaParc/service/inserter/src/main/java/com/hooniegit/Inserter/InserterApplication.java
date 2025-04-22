package com.hooniegit.Inserter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication 
public class InserterApplication {

    public static void main(String[] args) {
        SpringApplication.run(InserterApplication.class, args);
    }

}
