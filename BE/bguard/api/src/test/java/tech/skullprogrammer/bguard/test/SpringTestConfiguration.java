package tech.skullprogrammer.bguard.test;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(
        basePackages = {"tech.skullprogrammer.bguard.api", "tech.skullprogrammer.bguard.domain"}
)
@EnableJpaRepositories(basePackages = "tech.skullprogrammer.bguard.domain.repository")
@EntityScan(basePackages = "tech.skullprogrammer.bguard.domain.entity")
public class SpringTestConfiguration {
}
