package org.example.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import org.example.annotations.RequireRole;
import org.example.security.SecurityContext;

import java.util.logging.Logger;
import org.example.aop.InvocationContext;

@Aspect
@Component
@Order(2)
public class SecurityAspect {
    private final Logger logger = Logger.getLogger(SecurityAspect.class.getName());

    @Before("@annotation(requireRole)")
    public void checkRole(JoinPoint jp, RequireRole requireRole) {
        String required = requireRole.value();
        String current = SecurityContext.getRole();
        logger.info("[" + InvocationContext.currentId() + "] [Security] Required role='" + required + "', current='" + current + "' for " + jp.getSignature());
        if (current == null || !current.equals(required)) {
            throw new SecurityException("User does not have required role: " + required);
        }
    }
}
