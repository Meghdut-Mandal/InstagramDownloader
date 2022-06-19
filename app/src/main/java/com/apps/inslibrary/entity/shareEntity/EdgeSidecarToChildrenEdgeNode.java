package com.apps.inslibrary.entity.shareEntity;

import java.io.Serializable;
import java.util.List;

public class EdgeSidecarToChildrenEdgeNode implements Serializable {
    private List<DisplayResource> display_resources;
    private String display_url;

    private String id;
    private boolean is_video;
    private String shortcode;
    private String video_url;

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getShortcode() {
        return this.shortcode;
    }

    public void setShortcode(String str) {
        this.shortcode = str;
    }

    public String getDisplay_url() {
        return this.display_url;
    }

    public void setDisplay_url(String str) {
        this.display_url = str;
    }

    public List<DisplayResource> getDisplay_resources() {
        return this.display_resources;
    }

    public void setDisplay_resources(List<DisplayResource> list) {
        this.display_resources = list;
    }

    public boolean isIs_video() {
        return this.is_video;
    }

    public void setIs_video(boolean z) {
        this.is_video = z;
    }

    public String getVideo_url() {
        return this.video_url;
    }

    public void setVideo_url(String str) {
        this.video_url = str;
    }
}
