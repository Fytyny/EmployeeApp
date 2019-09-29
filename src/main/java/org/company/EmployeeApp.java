package org.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class EmployeeApp {
    public static void main(String[] args) {
        SpringApplication.run(EmployeeApp.class, args);
    }
}
