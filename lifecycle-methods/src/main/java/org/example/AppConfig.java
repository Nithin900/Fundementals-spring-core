package org.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public DBConnection dbConnection(){
        return new DBConnection("jdbc:mysql://localhost:3306/test", "root", "password)");
    }
}
