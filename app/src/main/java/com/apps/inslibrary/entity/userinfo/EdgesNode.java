package com.apps.inslibrary.entity.userinfo;

import com.apps.inslibrary.entity.Count;
import java.io.Serializable;
import java.util.List;

public class EdgesNode implements Serializable {
    private String __typename;
    private String accessibility_caption;
    private String display_url;
    private Count edge_liked_by;
    private Count edge_media_preview_like;
    private Caption edge_media_to_caption;
    private Count edge_media_to_comment;
    private EdgeOwnerToTimelineMedia edge_sidecar_to_children;

    private long id;
    private boolean is_video;
    private EdgesNodeOwner owner;
    private String shortcode;
    private List<Thumbnail> thumbnail_resources;
    private String thumbnail_src;
    private String video_url;

    public String get__typename() {
        return this.__typename;
    }

    public void set__typename(String str) {
        this.__typename = str;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long j) {
        this.id = j;
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

    public EdgesNodeOwner getOwner() {
        return this.owner;
    }

    public void setOwner(EdgesNodeOwner edgesNodeOwner) {
        this.owner = edgesNodeOwner;
    }

    public boolean isIs_video() {
        return this.is_video;
    }

    public void setIs_video(boolean z) {
        this.is_video = z;
    }

    public String getAccessibility_caption() {
        return this.accessibility_caption;
    }

    public void setAccessibility_caption(String str) {
        this.accessibility_caption = str;
    }

    public Caption getEdge_media_to_caption() {
        return this.edge_media_to_caption;
    }

    public void setEdge_media_to_caption(Caption caption) {
        this.edge_media_to_caption = caption;
    }

    public Count getEdge_media_to_comment() {
        return this.edge_media_to_comment;
    }

    public void setEdge_media_to_comment(Count count) {
        this.edge_media_to_comment = count;
    }

    public Count getEdge_liked_by() {
        return this.edge_liked_by;
    }

    public void setEdge_liked_by(Count count) {
        this.edge_liked_by = count;
    }

    public Count getEdge_media_preview_like() {
        return this.edge_media_preview_like;
    }

    public void setEdge_media_preview_like(Count count) {
        this.edge_media_preview_like = count;
    }

    public String getThumbnail_src() {
        return this.thumbnail_src;
    }

    public void setThumbnail_src(String str) {
        this.thumbnail_src = str;
    }

    public String getVideo_url() {
        return this.video_url;
    }

    public void setVideo_url(String str) {
        this.video_url = str;
    }

    public List<Thumbnail> getThumbnail_resources() {
        return this.thumbnail_resources;
    }

    public void setThumbnail_resources(List<Thumbnail> list) {
        this.thumbnail_resources = list;
    }

    public EdgeOwnerToTimelineMedia getEdge_sidecar_to_children() {
        return this.edge_sidecar_to_children;
    }

    public void setEdge_sidecar_to_children(EdgeOwnerToTimelineMedia edgeOwnerToTimelineMedia) {
        this.edge_sidecar_to_children = edgeOwnerToTimelineMedia;
    }
}
