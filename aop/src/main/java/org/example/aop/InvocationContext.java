package org.example.aop;

public class InvocationContext {
    private static final ThreadLocal<String> ID = new ThreadLocal<>();

    public static void setId(String id) {
        ID.set(id);
    }

    public static String currentId() {
        String id = ID.get();
        return id == null ? "-" : id;
    }

    public static void clear() {
        ID.remove();
    }
}
