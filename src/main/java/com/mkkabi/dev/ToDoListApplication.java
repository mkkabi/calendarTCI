package com.mkkabi.dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
//@EnableTransactionManagement
public class ToDoListApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ToDoListApplication.class, args);
    }

}
