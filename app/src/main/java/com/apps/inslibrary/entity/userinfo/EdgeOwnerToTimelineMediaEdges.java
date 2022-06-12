package com.apps.inslibrary.entity.userinfo;

import java.io.Serializable;

public class EdgeOwnerToTimelineMediaEdges implements Serializable {
    private EdgesNode node;

    public EdgesNode getNode() {
        return this.node;
    }

    public void setNode(EdgesNode edgesNode) {
        this.node = edgesNode;
    }
}
