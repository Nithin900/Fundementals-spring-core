package org.example.config;

import org.example.services.BookService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@Configuration
@ComponentScan(basePackages = "org.example")
public class ProjectConfig {
    // Beans configuration if any
}
