package org.example.aop;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.example.aop.InvocationContext;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(2)
public class LoggingAspect {
    private Logger logger= Logger.getLogger(LoggingAspect.class.getName());

    @Before("execution(* org.example.services.BookService.*(..))")
    public void beforeAdvice(JoinPoint joinPoint){
        System.out.println("[" + InvocationContext.currentId() + "] Before method: " + joinPoint.getSignature().getName());
    }

    @After("execution(* org.example.services.BookService.*(..))")
    public void afterAdvice(JoinPoint joinPoint){
        System.out.println("[" + InvocationContext.currentId() + "] After method: " + joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "execution(* org.example.services.BookService.*(..))", returning = "result")
    public void afterReturningAdvice(JoinPoint joinPoint, Object result){
        System.out.println("[" + InvocationContext.currentId() + "] After returning from method: " + joinPoint.getSignature().getName()+", result: "+ result);
    }

    @AfterThrowing(pointcut = "execution(* org.example.services.BookService.*(..))", throwing = "exception")
    public void afterThrowingAdvice(JoinPoint joinPoint, Exception exception){
        System.out.println(Level.SEVERE+"After thrown from method: " + joinPoint.getSignature().getName() + ", Exception: " + exception.getClass().getSimpleName() + ", Message: " + exception.getMessage());
    }

    // This method intercepts the execution of all methods within the org.example.services package and its subpackages.
    @Around("execution(* org.example.services.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable{
        // Logs a message indicating the start of method execution, including method signature.
        logger.info("[" + InvocationContext.currentId() + "] " + joinPoint.getSignature().toString()+" method execution starts...");

        // Records the start time of method execution using Instant.
        Instant start= Instant.now();

        // Proceeds with the actual method execution.
        Object result= joinPoint.proceed();

        // Records the end time of method execution using Instant.
        Instant end= Instant.now();

        long timeElapsed= Duration.between(start, end).toMillis();
        logger.info("[" + InvocationContext.currentId() + "] Execution time: "+ timeElapsed+" ms");

        // Logs a message indicating the end of method execution, including method signature.
        logger.info("[" + InvocationContext.currentId() + "] " + joinPoint.getSignature().toString()+" method execution ends.");

        // The below commented-out code block provides an alternative implementation using System.currentTimeMillis() for timing.
        // However, using Instant and Duration provides more precise timing and is recommended.
//        long startTime= System.currentTimeMillis();
//        System.out.println("Method " + joinPoint.getSignature().getName() + " execution started.");
//
//        Object result= joinPoint.proceed();
//
//        long endTime= System.currentTimeMillis();
//        System.out.println("Method " + joinPoint.getSignature().getName() + " execution finished. Time taken: " + (endTime - startTime) + " ms.");

        // Returns the result of the method execution.
        return result;
    }
}
