package org.example.aop;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;
import org.example.aop.InvocationContext;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class CustomAnnotationAspect {
    private static final Logger logger = Logger.getLogger(CustomAnnotationAspect.class.getName());

    @Around("@annotation(org.example.annotations.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        // Logs a message indicating the start of method execution, including method signature.
        logger.info("[" + InvocationContext.currentId() + "] " + joinPoint.getSignature().toString() + " method execution starts...");

        // Records the start time of method execution using Instant.
        Instant start = Instant.now();

        // Proceeds with the actual method execution.
        Object result = joinPoint.proceed();

        // Records the end time of method execution using Instant.
        Instant end = Instant.now();

        // Calculates the elapsed time for method execution using Duration.
        long timeElapsed = Duration.between(start, end).toMillis();

        // Logs the execution time of the method.
        logger.info("[" + InvocationContext.currentId() + "] Execution time: " + timeElapsed + " ms");

        // Logs a message indicating the end of method execution, including method signature.
        logger.info("[" + InvocationContext.currentId() + "] " + joinPoint.getSignature().toString() + " method execution ends.");

        // Returns the result of the method execution.
        return result;
    }
}
