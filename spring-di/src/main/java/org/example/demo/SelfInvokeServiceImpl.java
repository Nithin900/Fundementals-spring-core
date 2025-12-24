package org.example.demo;

import org.springframework.stereotype.Component;

@Component
public class SelfInvokeServiceImpl implements SelfInvokeService {

    @Override
    public void outer() {
        System.out.println("[DEMO] outer() called -> calling inner() via this.inner() (SELF INVOCATION)");
        // Self-invocation: bypasses proxy/interceptors
        this.inner();
    }

    @Override
    public void inner() {
        System.out.println("[DEMO] inner() executed");
    }
}
