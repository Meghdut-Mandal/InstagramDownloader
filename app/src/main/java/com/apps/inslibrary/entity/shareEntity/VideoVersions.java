package com.apps.inslibrary.entity.shareEntity;

import java.io.Serializable;

public class VideoVersions implements Serializable {
    private Integer height;

    /* renamed from: id */
    private String f184id;
    private Integer type;
    private String url;
    private Integer width;

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer num) {
        this.type = num;
    }

    public Integer getWidth() {
        return this.width;
    }

    public void setWidth(Integer num) {
        this.width = num;
    }

    public Integer getHeight() {
        return this.height;
    }

    public void setHeight(Integer num) {
        this.height = num;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public String getId() {
        return this.f184id;
    }

    public void setId(String str) {
        this.f184id = str;
    }
}
