package com.apps.inslibrary.entity.shareEntity;

import java.io.Serializable;

public class EdgeSidecarToChildrenEdge implements Serializable {
    private EdgeSidecarToChildrenEdgeNode node;

    public EdgeSidecarToChildrenEdgeNode getNode() {
        return this.node;
    }

    public void setNode(EdgeSidecarToChildrenEdgeNode edgeSidecarToChildrenEdgeNode) {
        this.node = edgeSidecarToChildrenEdgeNode;
    }
}
