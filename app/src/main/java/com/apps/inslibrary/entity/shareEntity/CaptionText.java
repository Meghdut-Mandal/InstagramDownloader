package com.apps.inslibrary.entity.shareEntity;

import java.io.Serializable;

public class CaptionText implements Serializable {
    private String text;

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }
}
