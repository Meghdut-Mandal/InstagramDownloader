package com.apps.inslibrary.entity;

public class PageParams {
    private String after;
    private int first;

    /* renamed from: id */
    private String f175id;

    public PageParams() {
    }

    public PageParams(String str, int i, String str2) {
        this.f175id = str;
        this.first = i;
        this.after = str2;
    }

    public String toString() {
        return "{\"id\":\"" + this.f175id + "\",\"first\":" + this.first + ",\"after\":\"" + this.after + "\"}";
    }

    public String getId() {
        return this.f175id;
    }

    public void setId(String str) {
        this.f175id = str;
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
