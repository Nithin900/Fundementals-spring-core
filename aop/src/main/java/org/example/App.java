package org.example;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.example.config.ProjectConfig;
import org.example.security.SecurityContext;
import org.example.services.BookService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Spring AOP Project
 *
 */
public class App 
{
    public static void main( String[] args )
    {
         // Spring logging: this may or may not show much depending on handler config
        Logger logger = Logger.getLogger("org.springframework");
        logger.setLevel(Level.FINE);

        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(ProjectConfig.class);

        BookService bookService = ctx.getBean(BookService.class);

        // 1) PROVE AOP proxying
        System.out.println("bookService runtime class = " + bookService.getClass());
        System.out.println("Is JDK proxy? " + java.lang.reflect.Proxy.isProxyClass(bookService.getClass()));

        // set a simulated user role and demonstrate security aspect
        SecurityContext.setRole("USER");
        bookService.addBook("Pride and Prejudice", "Jane Austen");
        bookService.addBook("The Catcher in the Rye", "J.D. Salinger");

        bookService.addBook("To Kill a Mockingbird", "Harper Lee");
        // attempt remove as USER (should fail due to require ADMIN)
        try {
            bookService.removeBook("To Kill a Mockingbird");
        } catch (Exception e) {
            System.out.println("removeBook failed for USER role: " + e.getMessage());
        }

        // promote to ADMIN and retry
        SecurityContext.setRole("ADMIN");
        bookService.removeBook("To Kill a Mockingbird");

        // demonstrate validation and retry: empty title will throw
        try {
            bookService.removeBook("");
        } catch (Exception e) {
            System.out.println("removeBook failed for empty title: " + e.getMessage());
        }

        bookService.findBook("1984");

        // 3) Clean shutdown (good habit; required for lifecycle demos in other modules)
        ctx.close();
    }
}
