package com.apps.inslibrary.entity.shareEntity;

import com.apps.inslibrary.entity.Count;
import java.io.Serializable;

public class ShortcodeMediaOwner implements Serializable {
    private Count edge_followed_by;
    private Count edge_owner_to_timeline_media;
    private String full_name;

    /* renamed from: id */
    private long f182id;

    /* renamed from: pk */
    private long f183pk;
    private String profile_pic_url;
    private String username;

    public long getId() {
        return this.f182id;
    }

    public void setId(long j) {
        this.f182id = j;
    }

    public String getProfile_pic_url() {
        return this.profile_pic_url;
    }

    public void setProfile_pic_url(String str) {
        this.profile_pic_url = str;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String str) {
        this.username = str;
    }

    public String getFull_name() {
        return this.full_name;
    }

    public void setFull_name(String str) {
        this.full_name = str;
    }

    public Count getEdge_owner_to_timeline_media() {
        return this.edge_owner_to_timeline_media;
    }

    public void setEdge_owner_to_timeline_media(Count count) {
        this.edge_owner_to_timeline_media = count;
    }

    public Count getEdge_followed_by() {
        return this.edge_followed_by;
    }

    public void setEdge_followed_by(Count count) {
        this.edge_followed_by = count;
    }

    public long getPk() {
        return this.f183pk;
    }

    public void setPk(long j) {
        this.f183pk = j;
    }
}
