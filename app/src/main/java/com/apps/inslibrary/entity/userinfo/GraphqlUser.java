package com.apps.inslibrary.entity.userinfo;

import com.apps.inslibrary.entity.Count;
import java.io.Serializable;

public class GraphqlUser implements Serializable {
    private String biography;
    private Count edge_follow;
    private Count edge_followed_by;
    private EdgeOwnerToTimelineMedia edge_owner_to_timeline_media;
    private long fbid;
    private String full_name;

    /* renamed from: id */
    private String f200id;
    private String profile_pic_url;
    private String profile_pic_url_hd;
    private String username;

    public String getBiography() {
        return this.biography;
    }

    public void setBiography(String str) {
        this.biography = str;
    }

    public Count getEdge_followed_by() {
        return this.edge_followed_by;
    }

    public void setEdge_followed_by(Count count) {
        this.edge_followed_by = count;
    }

    public long getFbid() {
        return this.fbid;
    }

    public void setFbid(long j) {
        this.fbid = j;
    }

    public Count getEdge_follow() {
        return this.edge_follow;
    }

    public void setEdge_follow(Count count) {
        this.edge_follow = count;
    }

    public String getFull_name() {
        return this.full_name;
    }

    public void setFull_name(String str) {
        this.full_name = str;
    }

    public String getId() {
        return this.f200id;
    }

    public void setId(String str) {
        this.f200id = str;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String str) {
        this.username = str;
    }

    public String getProfile_pic_url() {
        return this.profile_pic_url;
    }

    public void setProfile_pic_url(String str) {
        this.profile_pic_url = str;
    }

    public String getProfile_pic_url_hd() {
        return this.profile_pic_url_hd;
    }

    public void setProfile_pic_url_hd(String str) {
        this.profile_pic_url_hd = str;
    }

    public EdgeOwnerToTimelineMedia getEdge_owner_to_timeline_media() {
        return this.edge_owner_to_timeline_media;
    }

    public void setEdge_owner_to_timeline_media(EdgeOwnerToTimelineMedia edgeOwnerToTimelineMedia) {
        this.edge_owner_to_timeline_media = edgeOwnerToTimelineMedia;
    }
}
