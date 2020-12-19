package com.example.chatspace;

public class GroupMessage {
    private String message, name,date,time;

    public GroupMessage() {
    }

    public GroupMessage(String message, String name, String date, String time) {
        this.message = message;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
