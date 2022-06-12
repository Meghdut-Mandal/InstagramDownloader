package com.apps.inslibrary.entity;

import java.io.Serializable;

public class InstagramUser implements Serializable {
    private String full_name;
    private String profile_pic_url;
    private long user_id;
    private int user_type = 0;
    private String username;

    public InstagramUser() {
    }

    public InstagramUser(int i, long j, String str, String str2, String str3) {
        this.user_type = i;
        this.user_id = j;
        this.username = str;
        this.full_name = str2;
        this.profile_pic_url = str3;
    }

    public int getUser_type() {
        return this.user_type;
    }

    public void setUser_type(int i) {
        this.user_type = i;
    }

    public long getUser_id() {
        return this.user_id;
    }

    public void setUser_id(long j) {
        this.user_id = j;
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

    public String getProfile_pic_url() {
        return this.profile_pic_url;
    }

    public void setProfile_pic_url(String str) {
        this.profile_pic_url = str;
    }
}
