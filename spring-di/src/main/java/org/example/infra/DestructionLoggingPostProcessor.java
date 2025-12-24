package org.example.infra;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

/**
 * Destruction-phase drill:
 * Logs BEFORE_DESTROY for beans when the context closes.
 * You MUST call ctx.close() to see this.
 */
public class DestructionLoggingPostProcessor implements DestructionAwareBeanPostProcessor, PriorityOrdered {

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        String target = (targetClass != null) ? targetClass.getName() : "null";

        System.out.println("[SPRING-DESTROY] BEFORE_DESTROY beanName=" + beanName
                + " runtimeClass=" + bean.getClass().getName()
                + " targetClass=" + target);
    }

    @Override
    public boolean requiresDestruction(Object bean) {
        return true;
    }
}
