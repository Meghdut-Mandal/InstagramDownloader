package com.apps.inslibrary.entity.shareEntity;

import java.io.Serializable;
import java.util.List;

public class EdgeSidecarToChildren implements Serializable {
    private List<EdgeSidecarToChildrenEdge> edges;

    public List<EdgeSidecarToChildrenEdge> getEdges() {
        return this.edges;
    }

    public void setEdges(List<EdgeSidecarToChildrenEdge> list) {
        this.edges = list;
    }
}
