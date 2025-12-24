package org.example.demo;

public class PrototypeThing {
    private final long createdAtNanos = System.nanoTime();
    public long createdAtNanos() { return createdAtNanos; }
}
