package ru.samokhin.labCheck.fw.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "ru.samokhin.labcheck")
@EnableJpaRepositories(basePackages = "ru.samokhin.labcheck.adapter.persistence")
@EntityScan(basePackages = "ru.samokhin.labcheck.domain")
public class LabCheckApplication {
    public static void main(String[] args) {
        SpringApplication.run(LabCheckApplication.class, args);
    }
}
