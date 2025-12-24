package org.example.infra;

import java.lang.reflect.Proxy;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

/**
 * Instance-phase drill:
 * Logs BEFORE_INIT and AFTER_INIT for your app beans.
 *
 * Key improvement: filters using TARGET CLASS package so JDK proxies (jdk.proxy2.*)
 * still get logged when their target is org.example.*.
 */
public class LifecycleLoggingBeanPostProcessor implements BeanPostProcessor, PriorityOrdered {

    private final Set<String> allowedTargetPackagePrefixes = Set.of("org.example");

    // set true if you want only AOP proxied beans
    private final boolean onlyProxies = false;

    private final ConcurrentMap<String, AtomicInteger> beforeInitCount = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, AtomicInteger> afterInitCount  = new ConcurrentHashMap<>();

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (!shouldLog(bean)) return bean;

        beforeInitCount.computeIfAbsent(beanName, k -> new AtomicInteger()).incrementAndGet();
        log("BEFORE_INIT", beanName, bean);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!shouldLog(bean)) return bean;

        afterInitCount.computeIfAbsent(beanName, k -> new AtomicInteger()).incrementAndGet();
        log("AFTER_INIT", beanName, bean);
        return bean;
    }

    private boolean shouldLog(Object bean) {
        if (bean == null) return false;

        Class<?> targetClass = AopUtils.getTargetClass(bean);
        String pkg = (targetClass != null && targetClass.getPackage() != null)
                ? targetClass.getPackage().getName()
                : "";

        boolean allowed = allowedTargetPackagePrefixes.stream().anyMatch(pkg::startsWith);
        if (!allowed) return false;

        if (onlyProxies && !AopUtils.isAopProxy(bean)) return false;

        return true;
    }

    private void log(String phase, String beanName, Object bean) {
        Class<?> runtimeClass = bean.getClass();
        boolean isProxy = AopUtils.isAopProxy(bean);
        Class<?> targetClass = AopUtils.getTargetClass(bean);

        StringBuilder sb = new StringBuilder(260);
        sb.append("[SPRING-LIFECYCLE] ").append(phase)
          .append(" beanName=").append(beanName)
          .append(" runtimeClass=").append(runtimeClass.getName())
          .append(" proxy=").append(isProxy);

        if (isProxy) {
            sb.append(" proxyType=").append(proxyType(runtimeClass))
              .append(" targetClass=").append(targetClass != null ? targetClass.getName() : "null");
        }

        System.out.println(sb);
    }

    private String proxyType(Class<?> runtimeClass) {
        if (Proxy.isProxyClass(runtimeClass)) return "JDK";
        String n = runtimeClass.getName();
        if (n.contains("$$SpringCGLIB$$") || n.contains("$$EnhancerBySpringCGLIB$$") || n.contains("$$")) return "CGLIB";
        return "UNKNOWN";
    }

    public ConcurrentMap<String, AtomicInteger> getBeforeInitCount() {
        return beforeInitCount;
    }

    public ConcurrentMap<String, AtomicInteger> getAfterInitCount() {
        return afterInitCount;
    }
}
