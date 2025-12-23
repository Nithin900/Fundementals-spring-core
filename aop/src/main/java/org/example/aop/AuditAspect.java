package org.example.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;
import org.example.aop.InvocationContext;

@Aspect
@Component
@Order(4)
public class AuditAspect {
    private final Logger logger = Logger.getLogger(AuditAspect.class.getName());

    @Before("execution(* org.example.services.*.*(..))")
    public void before(JoinPoint jp) {
        logger.info("[" + InvocationContext.currentId() + "] [Audit] BEFORE: " + jp.getSignature() + " args=" + java.util.Arrays.toString(jp.getArgs()));
    }

    @AfterReturning(pointcut = "execution(* org.example.services.*.*(..))", returning = "result")
    public void afterReturning(JoinPoint jp, Object result) {
        logger.info("[" + InvocationContext.currentId() + "] [Audit] AFTER: " + jp.getSignature() + " result=" + result);
    }
}
