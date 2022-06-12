package com.apps.inslibrary.entity.userinfo;

import java.io.Serializable;

public class CaptionEdges implements Serializable {
    private CaptionNode node;

    public CaptionNode getNode() {
        return this.node;
    }

    public void setNode(CaptionNode captionNode) {
        this.node = captionNode;
    }
}
