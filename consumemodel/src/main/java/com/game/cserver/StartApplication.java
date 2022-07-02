package com.game.cserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author zheng
 */
@SpringBootApplication(scanBasePackages = {"com.game.gateway","com.game.newwork","com.game.common"})
public class StartApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(StartApplication.class, args);
    }
}
