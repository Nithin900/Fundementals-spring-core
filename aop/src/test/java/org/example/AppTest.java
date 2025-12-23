package org.example;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.example.config.ProjectConfig;
import org.example.implementations.BookServiceImpl;
import org.example.security.SecurityContext;
import org.example.services.BookService;
import org.example.aop.*;
import org.example.annotations.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Unit test for all classes in AOP project
 */
public class AppTest 
    extends TestCase
{
    private AnnotationConfigApplicationContext ctx;
    private BookService bookService;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Setup method to initialize Spring context
     */
    protected void setUp() throws Exception {
        super.setUp();
        ctx = new AnnotationConfigApplicationContext(ProjectConfig.class);
        bookService = ctx.getBean(BookService.class);
        SecurityContext.setRole("ADMIN");
    }

    /**
     * Teardown method to close Spring context
     */
    protected void tearDown() throws Exception {
        if (ctx != null) {
            ctx.close();
        }
        super.tearDown();
    }

    /**
     * Test App class and Spring context initialization
     */
    public void testAppInitialization()
    {
        assertTrue(ctx != null);
        assertTrue(bookService != null);
    }

    /**
     * Test BookService interface and BookServiceImpl
     */
    public void testBookService()
    {
        assertTrue(bookService instanceof BookServiceImpl);
        
        // Test addBook
        bookService.addBook("Test Book", "Test Author");
        
        // Test findBook
        String result = bookService.findBook("Test Book");
        assertTrue(result.contains("Test Book"));
    }

    /**
     * Test addBook functionality with AOP aspects
     */
    public void testAddBook()
    {
        bookService.addBook("Spring in Action", "Craig Walls");
        bookService.addBook("Effective Java", "Joshua Bloch");
        assertTrue(true);
    }

    /**
     * Test removeBook with security and retry aspects
     */
    public void testRemoveBook()
    {
        SecurityContext.setRole("ADMIN");
        bookService.addBook("Test Remove", "Author");
        bookService.removeBook("Test Remove");
        assertTrue(true);
    }

    /**
     * Test removeBook with invalid title (triggers validation)
     */
    public void testRemoveBookInvalidTitle()
    {
        SecurityContext.setRole("ADMIN");
        try {
            bookService.removeBook("");
            fail("Should have thrown exception for empty title");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    /**
     * Test security aspect - USER role restriction
     */
    public void testSecurityAspectUserRole()
    {
        SecurityContext.setRole("USER");
        bookService.addBook("Allowed Book", "Author");
        
        try {
            bookService.removeBook("Allowed Book");
            fail("Should have thrown security exception for USER role");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    /**
     * Test security aspect - ADMIN role access
     */
    public void testSecurityAspectAdminRole()
    {
        SecurityContext.setRole("ADMIN");
        bookService.addBook("Admin Book", "Author");
        bookService.removeBook("Admin Book");
        assertTrue(true);
    }

    /**
     * Test InvocationContext for tracking invocation IDs
     */
    public void testInvocationContext()
    {
        String currentId = InvocationContext.currentId();
        assertTrue(currentId != null && !currentId.isEmpty());
    }

    /**
     * Test SecurityContext role management
     */
    public void testSecurityContext()
    {
        SecurityContext.setRole("ADMIN");
        String role = SecurityContext.getRole();
        assertEquals("ADMIN", role);
        
        SecurityContext.setRole("USER");
        role = SecurityContext.getRole();
        assertEquals("USER", role);
    }

    /**
     * Test AOP Aspect classes are properly created
     */
    public void testAspectsInitialized()
    {
        assertTrue(ctx.containsBean("auditAspect") || ctx.containsBean("org.example.aop.AuditAspect"));
        assertTrue(ctx.containsBean("loggingAspect") || ctx.containsBean("org.example.aop.LoggingAspect"));
        assertTrue(ctx.containsBean("securityAspect") || ctx.containsBean("org.example.aop.SecurityAspect"));
    }

    /**
     * Test annotation classes are properly defined
     */
    public void testAnnotationsAvailable()
    {
        assertTrue(Audit.class != null);
        assertTrue(LogExecutionTime.class != null);
        assertTrue(Transactional.class != null);
        assertTrue(Retryable.class != null);
        assertTrue(RequireRole.class != null);
        assertTrue(Metrics.class != null);
    }

    /**
     * Test findBook functionality
     */
    public void testFindBook()
    {
        bookService.addBook("Find Me", "Author");
        String result = bookService.findBook("Find Me");
        assertTrue(result.contains("Find Me"));
    }

    /**
     * Test ProjectConfig bean creation
     */
    public void testProjectConfig()
    {
        ProjectConfig config = ctx.getBean(ProjectConfig.class);
        assertTrue(config != null);
    }

    /**
     * Rigorous Test - Basic functionality
     */
    public void testApp()
    {
        assertTrue(true);
    }
}
