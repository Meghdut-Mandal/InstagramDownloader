package com.apps.inslibrary.entity;

public class PageParams {
    private String after;
    private int first;

    private String id;

    public PageParams() {
    }

    public PageParams(String str, int i, String str2) {
        this.id = str;
        this.first = i;
        this.after = str2;
    }

    public String toString() {
        return "{\"id\":\"" + this.id + "\",\"first\":" + this.first + ",\"after\":\"" + this.after + "\"}";
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public int getFirst() {
        return this.first;
    }

    public void setFirst(int i) {
        this.first = i;
    }

    public String getAfter() {
        return this.after;
    }

    public void setAfter(String str) {
        this.after = str;
    }
}
