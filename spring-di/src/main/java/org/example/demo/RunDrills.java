package org.example.demo;

import org.example.config.DemoConfig;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class RunDrills {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

        // Scan your project (aspects/components) and register this drill config
        ctx.scan("org.example");
        ctx.register(DemoConfig.class);

        System.out.println("=== REFRESH CONTEXT ===");
        ctx.refresh();

        System.out.println("\n=== SELF INVOCATION DRILL ===");
        SelfInvokeService s = ctx.getBean(SelfInvokeService.class);

        System.out.println("SelfInvokeService runtime class = " + s.getClass());
        System.out.println("Is AOP proxy? " + AopUtils.isAopProxy(s));

        System.out.println("\n-- Calling inner() from outside (should be advised) --");
        s.inner();

        System.out.println("\n-- Calling outer() (inner() called internally, should bypass advice) --");
        s.outer();

        System.out.println("\n=== PROTOTYPE DRILL ===");
        Object p1 = ctx.getBean("prototypeThing");
        Object p2 = ctx.getBean("prototypeThing");
        System.out.println("prototypeThing same instance? " + (p1 == p2));

        System.out.println("\n=== LAZY INIT DRILL ===");
        System.out.println("Requesting lazyThing now...");
        ctx.getBean("lazyThing"); // constructor prints here, not at refresh

        System.out.println("\n=== CLOSE CONTEXT (DESTROY DRILL) ===");
        ctx.close();
    }
}
