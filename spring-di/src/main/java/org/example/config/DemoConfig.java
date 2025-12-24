package org.example.config;

import org.example.demo.LazyThing;
import org.example.demo.PrototypeThing;
import org.example.infra.BeanDefinitionPhaseLogger;
import org.example.infra.DestructionLoggingPostProcessor;
import org.example.infra.LifecycleLoggingBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

/**
 * Wires the drill infrastructure + prototype + lazy beans.
 * Works alongside your existing @Component scanning/aspects.
 */
@Configuration
@EnableAspectJAutoProxy
public class DemoConfig {

    @Bean
    public BeanDefinitionPhaseLogger beanDefinitionPhaseLogger() {
        return new BeanDefinitionPhaseLogger();
    }

    @Bean
    public LifecycleLoggingBeanPostProcessor lifecycleLoggingBeanPostProcessor() {
        return new LifecycleLoggingBeanPostProcessor();
    }

    @Bean
    public DestructionLoggingPostProcessor destructionLoggingPostProcessor() {
        return new DestructionLoggingPostProcessor();
    }

    @Bean
    @Scope("prototype")
    public PrototypeThing prototypeThing() {
        return new PrototypeThing();
    }

    @Bean
    @Lazy
    public LazyThing lazyThing() {
        return new LazyThing();
    }
}
