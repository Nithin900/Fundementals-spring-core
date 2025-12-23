package org.example.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;
import org.example.aop.InvocationContext;

@Aspect
@Component
@Order(1)
public class TransactionalAspect {
    private final Logger logger = Logger.getLogger(TransactionalAspect.class.getName());

    @Around("@annotation(org.example.annotations.Transactional)")
    public Object aroundTransactional(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("[" + InvocationContext.currentId() + "] [Transaction] BEGIN: " + joinPoint.getSignature());
        try {
            Object result = joinPoint.proceed();
            logger.info("[" + InvocationContext.currentId() + "] [Transaction] COMMIT: " + joinPoint.getSignature());
            return result;
        } catch (Throwable t) {
            logger.severe("[" + InvocationContext.currentId() + "] [Transaction] ROLLBACK: " + joinPoint.getSignature() + " due to " + t.getClass().getSimpleName());
            throw t;
        }
    }
}
