package org.example.demo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * AOP drill:
 * Should wrap calls to SelfInvokeService.inner(..) when invoked THROUGH proxy.
 */
@Aspect
@Component
@Order(1)
public class TraceAspect {

    @Around("execution(* org.example.demo.SelfInvokeService.inner(..))")
    public Object aroundInner(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("[TRACE] BEFORE inner()");
        try {
            return pjp.proceed();
        } finally {
            System.out.println("[TRACE] AFTER inner()");
        }
    }
}
