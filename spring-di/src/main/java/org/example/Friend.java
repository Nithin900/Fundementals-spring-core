package org.example;

import org.springframework.stereotype.Component;

@Component
public class Friend {
    private int fid;
    private String name;

    public Friend() {
    }

    public Friend(int fid, String name) {
        this.fid = fid;
        this.name = name;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "fid=" + fid +
                ", name='" + name + '\'' +
                '}';
    }
}
