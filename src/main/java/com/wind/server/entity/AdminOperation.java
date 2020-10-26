package com.wind.server.entity;

public class AdminOperation {
    String type;
    String ip;
    String time;
    String session;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public AdminOperation(String type, String ip, String time, String session) {
        this.type = type;
        this.ip = ip;
        this.time = time;
        this.session = session;
    }
}
