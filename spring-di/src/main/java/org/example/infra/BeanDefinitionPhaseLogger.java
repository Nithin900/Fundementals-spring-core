package org.example.infra;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

/**
 * Definition-phase drill:
 * Runs BEFORE any bean instances are created.
 * Logs BeanDefinitions (blueprints).
 */
public class BeanDefinitionPhaseLogger implements BeanFactoryPostProcessor, PriorityOrdered {

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) throws BeansException {
        System.out.println("[SPRING-DEFINITION] ---- BeanDefinitions (before instantiation) ----");

        String[] names = bf.getBeanDefinitionNames();
        for (String name : names) {
            BeanDefinition bd = bf.getBeanDefinition(name);

            String beanClassName = bd.getBeanClassName(); // null for @Bean factory-method definitions, etc.

            // Reduce noise: show only org.example beans when class name is known
            if (beanClassName != null && !beanClassName.startsWith("org.example")) {
                continue;
            }

            String scope = (bd.getScope() == null || bd.getScope().isEmpty()) ? "singleton(default)" : bd.getScope();
            System.out.println("[SPRING-DEFINITION] name=" + name
                    + " class=" + (beanClassName != null ? beanClassName : "<factory-method/unknown>")
                    + " scope=" + scope
                    + " lazyInit=" + bd.isLazyInit());
        }

        System.out.println("[SPRING-DEFINITION] -----------------------------------------------");
    }
}
