package com.apps.inslibrary.entity.shareEntity;

import java.io.Serializable;

public class DisplayResource implements Serializable {
    private int config_height;
    private int config_width;
    private String src;

    public String getSrc() {
        return this.src;
    }

    public void setSrc(String str) {
        this.src = str;
    }

    public int getConfig_width() {
        return this.config_width;
    }

    public void setConfig_width(int i) {
        this.config_width = i;
    }

    public int getConfig_height() {
        return this.config_height;
    }

    public void setConfig_height(int i) {
        this.config_height = i;
    }
}
