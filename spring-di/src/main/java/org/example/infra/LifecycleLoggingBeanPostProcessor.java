package org.example.infra;

import java.lang.reflect.Proxy;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

/**
 * Logs bean lifecycle visibility points:
 * - postProcessBeforeInitialization
 * - postProcessAfterInitialization (where proxies often appear)
 *
 * Spring 6 / Java 17 compatible.
 *
 * IMPORTANT:
 * - This observes INSTANCE lifecycle hooks (not BeanDefinition time).
 * - For BeanDefinition phase, use a BeanFactoryPostProcessor.
 */
public class LifecycleLoggingBeanPostProcessor
        implements BeanPostProcessor, PriorityOrdered, DisposableBean {

    // Change this to your root package(s). Since your groupId is org.example, this is a good default:
    private final Set<String> allowedPackagePrefixes = new LinkedHashSet<>(Set.of(
            "org.example"
    ));

    // Set to true if you want to see a lot of framework beans too (usually too noisy)
    private final boolean includeFrameworkBeans = false;

    // Set to true if you only want to see beans that become AOP proxies
    private final boolean onlyProxies = false;

    private final ConcurrentMap<String, AtomicInteger> beforeInitCount = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, AtomicInteger> afterInitCount  = new ConcurrentHashMap<>();
    private final AtomicInteger totalLogged = new AtomicInteger(0);

    @Override
    public int getOrder() {
        // Run early to observe other processors/proxying effects
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (!shouldLog(bean, beanName)) return bean;

        beforeInitCount.computeIfAbsent(beanName, k -> new AtomicInteger()).incrementAndGet();
        log(bean, beanName, "BEFORE_INIT");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!shouldLog(bean, beanName)) return bean;

        afterInitCount.computeIfAbsent(beanName, k -> new AtomicInteger()).incrementAndGet();
        log(bean, beanName, "AFTER_INIT");
        return bean;
    }

    private boolean shouldLog(Object bean, String beanName) {
        if (bean == null) return false;

        Class<?> runtimeClass = bean.getClass();
        String pkg = (runtimeClass.getPackage() != null) ? runtimeClass.getPackage().getName() : "";

        boolean isFramework = pkg.startsWith("org.springframework") || pkg.startsWith("jakarta.");
        if (!includeFrameworkBeans && isFramework) return false;

        boolean allowedByPkg = allowedPackagePrefixes.stream().anyMatch(pkg::startsWith);
        if (!allowedByPkg) return false;

        if (onlyProxies && !AopUtils.isAopProxy(bean)) return false;

        return true;
    }

    private void log(Object bean, String beanName, String phase) {
        totalLogged.incrementAndGet();

        Class<?> runtimeClass = bean.getClass();
        boolean isProxy = AopUtils.isAopProxy(bean);
        Class<?> targetClass = AopUtils.getTargetClass(bean);

        StringBuilder sb = new StringBuilder(256);
        sb.append("[SPRING-LIFECYCLE] ")
          .append(phase)
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

    @Override
    public void destroy() {
        // Runs when the ApplicationContext closes (if you close it)
        System.out.println("[SPRING-LIFECYCLE] ---- SUMMARY ----");
        System.out.println("[SPRING-LIFECYCLE] totalLoggedEvents=" + totalLogged.get());
        System.out.println("[SPRING-LIFECYCLE] beforeInitCount=" + beforeInitCount);
        System.out.println("[SPRING-LIFECYCLE] afterInitCount=" + afterInitCount);
        System.out.println("[SPRING-LIFECYCLE] ----------------");
    }
}
