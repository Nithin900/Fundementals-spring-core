package org.example.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import org.example.annotations.Retryable;

import java.util.logging.Logger;
import org.example.aop.InvocationContext;

@Aspect
@Component
@Order(5)
public class RetryAspect {
    private final Logger logger = Logger.getLogger(RetryAspect.class.getName());

    @Around("@annotation(retryable)")
    public Object aroundRetry(ProceedingJoinPoint pjp, Retryable retryable) throws Throwable {
        int attempts = retryable.attempts();
        long delay = retryable.delayMs();
        int tried = 0;
        while (true) {
            try {
                tried++;
                return pjp.proceed();
            } catch (Throwable t) {
                if (tried >= attempts) {
                    logger.severe("[" + InvocationContext.currentId() + "] [Retry] all attempts failed for " + pjp.getSignature());
                    throw t;
                }
                logger.warning("[" + InvocationContext.currentId() + "] [Retry] attempt " + tried + " failed for " + pjp.getSignature() + ", retrying after " + delay + "ms");
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw ie;
                }
            }
        }
    }
}
