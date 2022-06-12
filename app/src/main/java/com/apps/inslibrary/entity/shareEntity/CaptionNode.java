package com.apps.inslibrary.entity.shareEntity;

import java.io.Serializable;

public class CaptionNode implements Serializable {
    private CaptionText node;

    public CaptionText getNode() {
        return this.node;
    }

    public void setNode(CaptionText captionText) {
        this.node = captionText;
    }
}
