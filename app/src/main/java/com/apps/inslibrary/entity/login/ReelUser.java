package com.apps.inslibrary.entity.login;

import java.io.Serializable;

public class ReelUser implements Serializable {
    private String __typename;

    /* renamed from: id */
    private String id;
    private String profile_pic_url;
    private String username;

    public String get__typename() {
        return this.__typename;
    }

    public void set__typename(String str) {
        this.__typename = str;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
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
