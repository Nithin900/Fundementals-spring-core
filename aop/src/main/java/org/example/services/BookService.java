package org.example.services;

import org.example.annotations.LogExecutionTime;
import org.springframework.stereotype.Component;

@Component
public interface BookService {
    void addBook(String title, String author);
    void removeBook(String title);

    String findBook(String title);
}
