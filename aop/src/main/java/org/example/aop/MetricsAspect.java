package org.example.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import org.example.annotations.Metrics;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import org.example.aop.InvocationContext;

@Aspect
@Component
@Order(3)
public class MetricsAspect {
    private static final Logger logger = Logger.getLogger(MetricsAspect.class.getName());
    private static final ConcurrentHashMap<String, AtomicLong> counters = new ConcurrentHashMap<>();

    @Around("@annotation(metrics)")
    public Object aroundMetrics(ProceedingJoinPoint pjp, Metrics metrics) throws Throwable {
        String name = metrics.name().isEmpty() ? pjp.getSignature().toShortString() : metrics.name();
        counters.computeIfAbsent(name, k -> new AtomicLong()).incrementAndGet();
        long before = System.currentTimeMillis();
        Object result = pjp.proceed();
        long after = System.currentTimeMillis();
        logger.info("[" + InvocationContext.currentId() + "] [Metrics] " + name + " count=" + counters.get(name).get() + " duration=" + (after - before) + "ms");
        return result;
    }
}
