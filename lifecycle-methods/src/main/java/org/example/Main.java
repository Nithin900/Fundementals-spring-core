package org.example;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        // Initialize Spring context
        var context= new AnnotationConfigApplicationContext(AppConfig.class);

        // Get DBConnection bean
        DBConnection dbConnection= context.getBean(DBConnection.class);

        // Use DBConnection
        // ...

        // Close Spring context
        context.close();
    }
}