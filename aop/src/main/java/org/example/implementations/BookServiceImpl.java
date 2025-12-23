package org.example.implementations;

import org.example.annotations.Audit;
import org.example.annotations.LogExecutionTime;
import org.example.annotations.Metrics;
import org.example.annotations.RequireRole;
import org.example.annotations.Retryable;
import org.example.annotations.Transactional;
import org.example.services.BookService;
import org.springframework.stereotype.Component;

@Component
public class BookServiceImpl implements BookService {
    @LogExecutionTime
    @Override
    public void addBook(String title, String author) {
        System.out.println("[" + org.example.aop.InvocationContext.currentId() + "] Adding book: " + title + " by " + author);
    }

    @LogExecutionTime
    @Transactional
    @RequireRole("ADMIN")
    @Metrics(name = "book.remove")
    @Audit
    @Retryable(attempts = 3, delayMs = 300)
    public void removeBook(String title) {
        if (title == null || title.isEmpty()) {
            throw new RuntimeException("Book title is null!");
        }
        System.out.println("[" + org.example.aop.InvocationContext.currentId() + "] Removing book: " + title);
        System.out.println("[" + org.example.aop.InvocationContext.currentId() + "] Book removed: " + title);
    }

    @Override
    public String findBook(String title) {
        System.out.println("[" + org.example.aop.InvocationContext.currentId() + "] Finding book: " + title);
        return "Book found: " + title;
    }
}
