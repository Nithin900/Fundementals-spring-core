package org.example;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private Connection connection;
    private String url;
    private String username;
    private String password;

    public DBConnection(String url, String username, String password) {
        this.url = url;
        this.username= username;
        this.password= password;
    }

    @PostConstruct
    public void init(){
        try{
            System.out.println("Initializing db connection...");
            connection= DriverManager.getConnection(url, username, password);
            System.out.println("Database connection initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Error initializing db connection: "+e.getMessage());
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void destroy(){
        try{
            if(connection != null && !connection.isClosed()){
                System.out.println("Closing database connection...");
                connection.close();
                System.out.println("Database connection closed successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
