package com.apps.inslibrary.entity.userinfo;

import java.io.Serializable;
import java.util.List;

public class Caption implements Serializable {
    private List<CaptionEdges> edges;

    public List<CaptionEdges> getEdges() {
        return this.edges;
    }

    public void setEdges(List<CaptionEdges> list) {
        this.edges = list;
    }
}
