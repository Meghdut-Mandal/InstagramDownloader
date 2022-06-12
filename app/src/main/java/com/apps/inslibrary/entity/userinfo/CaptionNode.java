package com.apps.inslibrary.entity.userinfo;

import java.io.Serializable;

public class CaptionNode implements Serializable {
    private String text;

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }
}
