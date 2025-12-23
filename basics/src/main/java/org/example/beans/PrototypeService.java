package org.example.beans;

import java.util.UUID;

/**
 * This bean is configured with @Scope("prototype")
 * A new instance is created every time this bean is requested from the context
 */
public class PrototypeService {
    private final String instanceId;
    private final long creationTime;

    public PrototypeService() {
        this.instanceId = UUID.randomUUID().toString();
        this.creationTime = System.currentTimeMillis();
        System.out.println("Creating new PrototypeService instance: " + instanceId);
    }

    public String getInstanceId() {
        return instanceId;
    }

    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public String toString() {
        return "PrototypeService{" +
                "instanceId='" + instanceId + '\'' +
                ", creationTime=" + creationTime +
                '}';
    }
}
