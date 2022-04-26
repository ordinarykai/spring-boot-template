package com.kai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author kai
 */
@Slf4j
@SpringBootApplication
public class Application {

    public static ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(Application.class, args);
    }

}
