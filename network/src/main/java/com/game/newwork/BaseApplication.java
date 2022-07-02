package com.game.newwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.game.newwork")
public class BaseApplication {

    public static void main(String[] args) {

        SpringApplication.run(BaseApplication.class, args);
    }

}
