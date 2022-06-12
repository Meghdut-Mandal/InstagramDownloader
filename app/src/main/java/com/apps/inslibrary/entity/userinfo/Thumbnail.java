package com.apps.inslibrary.entity.userinfo;

import java.io.Serializable;

public class Thumbnail implements Serializable {
    private int configHeight;
    private int configWidth;
    private String src;

    public String getSrc() {
        return this.src;
    }

    public void setSrc(String str) {
        this.src = str;
    }

    public int getConfigWidth() {
        return this.configWidth;
    }

    public void setConfigWidth(int i) {
        this.configWidth = i;
    }

    public int getConfigHeight() {
        return this.configHeight;
    }

    public void setConfigHeight(int i) {
        this.configHeight = i;
    }
}
