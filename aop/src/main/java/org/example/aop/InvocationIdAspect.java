package org.example.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.logging.Logger;

@Aspect
@Component
@Order(0)
public class InvocationIdAspect {
    private static final Logger logger = Logger.getLogger(InvocationIdAspect.class.getName());

    @Around("execution(* org.example.services.*.*(..))")
    public Object wrapWithId(ProceedingJoinPoint pjp) throws Throwable {
        String id = UUID.randomUUID().toString().substring(0, 8);
        InvocationContext.setId(id);
        logger.info("[" + id + "] START " + pjp.getSignature());
        try {
            return pjp.proceed();
        } finally {
            logger.info("[" + id + "] END   " + pjp.getSignature());
            InvocationContext.clear();
        }
    }
}
