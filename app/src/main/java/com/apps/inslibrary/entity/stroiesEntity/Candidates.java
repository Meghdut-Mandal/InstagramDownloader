package com.apps.inslibrary.entity.stroiesEntity;

import java.io.Serializable;

public class Candidates implements Serializable {
    private int height;
    private String url;
    private int width;

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int i) {
        this.height = i;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }
}
