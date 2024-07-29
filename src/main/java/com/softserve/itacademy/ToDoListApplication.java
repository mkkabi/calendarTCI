package com.softserve.itacademy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;


//Ensure that your Spring configuration is set up correctly, including the @EnableTransactionManagement
// annotation in your configuration class and that you have a valid PlatformTransactionManager bean defined.
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
//@EnableTransactionManagement
public class ToDoListApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ToDoListApplication.class, args);
    }

}
