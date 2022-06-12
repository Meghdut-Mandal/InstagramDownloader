package com.apps.inslibrary.entity.stroiesEntity;

import java.io.Serializable;

public class VideoVersions implements Serializable {
    private int height;

    /* renamed from: id */
    private String f192id;
    private int type;
    private String url;
    private int width;

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

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

    public String getId() {
        return this.f192id;
    }

    public void setId(String str) {
        this.f192id = str;
    }
}
