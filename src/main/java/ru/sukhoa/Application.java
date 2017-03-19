package ru.sukhoa;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication()
@EntityScan("ru.sukhoa.domain")
public class Application {
    final static Logger logger = Logger.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("app started");
        SpringApplication.run(Application.class, args);
    }
}