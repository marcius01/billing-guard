package tech.skullprogrammer.bguard.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@SpringBootApplication
@ComponentScan(basePackages = {"tech.skullprogrammer.bguard "})
@EntityScan(basePackages = {"tech.skullprogrammer.bguard.domain"})
@EnableJpaRepositories(basePackages = {"tech.skullprogrammer.bguard.domain"})
public class AppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

}
