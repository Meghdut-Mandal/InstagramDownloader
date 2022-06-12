package com.apps.inslibrary.entity.shareEntity;

import java.io.Serializable;
import java.util.List;

public class EdgeMediaToCaption implements Serializable {
    private List<CaptionNode> edges;

    public List<CaptionNode> getEdges() {
        return this.edges;
    }

    public void setEdges(List<CaptionNode> list) {
        this.edges = list;
    }
}
