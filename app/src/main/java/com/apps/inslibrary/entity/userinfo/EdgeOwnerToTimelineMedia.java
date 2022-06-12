package com.apps.inslibrary.entity.userinfo;

import java.io.Serializable;
import java.util.List;

public class EdgeOwnerToTimelineMedia implements Serializable {
    private int count;
    private List<EdgeOwnerToTimelineMediaEdges> edges;
    private PageInfo page_info;

    public int getCount() {
        return this.count;
    }

    public void setCount(int i) {
        this.count = i;
    }

    public PageInfo getPageInfo() {
        return this.page_info;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.page_info = pageInfo;
    }

    public List<EdgeOwnerToTimelineMediaEdges> getEdges() {
        return this.edges;
    }

    public void setEdges(List<EdgeOwnerToTimelineMediaEdges> list) {
        this.edges = list;
    }
}
