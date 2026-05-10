package tech.skullprogrammer.bguard.test;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"tech.skullprogrammer.bguard.api"})
public class SpringTestConfiguration {
}
