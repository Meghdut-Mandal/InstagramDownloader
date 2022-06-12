package com.apps.inslibrary.entity.stroiesEntity;

import java.io.Serializable;

public class StoriesUser implements Serializable {

    /* renamed from: id */
    private String f191id;
    private String profile_pic_url;
    private String username;

    public String getId() {
        return this.f191id;
    }

    public void setId(String str) {
        this.f191id = str;
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
}
