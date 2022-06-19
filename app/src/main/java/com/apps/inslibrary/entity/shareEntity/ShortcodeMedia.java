package com.apps.inslibrary.entity.shareEntity;

import java.io.Serializable;
import java.util.List;

public class ShortcodeMedia implements Serializable {
    private String accessibility_caption;
    private List<DisplayResource> display_resources;
    private String display_url;
    private EdgeMediaToCaption edge_media_to_caption;
    private EdgeSidecarToChildren edge_sidecar_to_children;

    private String id;
    private boolean is_video;
    private ShortcodeMediaOwner owner;
    private String shortcode;
    private String thumbnail_src;
    private double video_duration;
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

    public double getVideo_duration() {
        return this.video_duration;
    }

    public void setVideo_duration(double d) {
        this.video_duration = d;
    }

    public EdgeMediaToCaption getEdge_media_to_caption() {
        return this.edge_media_to_caption;
    }

    public void setEdge_media_to_caption(EdgeMediaToCaption edgeMediaToCaption) {
        this.edge_media_to_caption = edgeMediaToCaption;
    }

    public String getThumbnail_src() {
        return this.thumbnail_src;
    }

    public void setThumbnail_src(String str) {
        this.thumbnail_src = str;
    }

    public ShortcodeMediaOwner getOwner() {
        return this.owner;
    }

    public void setOwner(ShortcodeMediaOwner shortcodeMediaOwner) {
        this.owner = shortcodeMediaOwner;
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

    public EdgeSidecarToChildren getEdge_sidecar_to_children() {
        return this.edge_sidecar_to_children;
    }

    public void setEdge_sidecar_to_children(EdgeSidecarToChildren edgeSidecarToChildren) {
        this.edge_sidecar_to_children = edgeSidecarToChildren;
    }

    public String getAccessibility_caption() {
        return this.accessibility_caption;
    }

    public void setAccessibility_caption(String str) {
        this.accessibility_caption = str;
    }
}
