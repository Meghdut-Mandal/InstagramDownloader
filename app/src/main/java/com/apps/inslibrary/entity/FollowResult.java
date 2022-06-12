package com.apps.inslibrary.entity;

import java.util.List;

public class FollowResult {
    private String max_id;
    private List<InstagramUser> users;

    public List<InstagramUser> getUsers() {
        return this.users;
    }

    public void setUsers(List<InstagramUser> list) {
        this.users = list;
    }

    public String getMax_id() {
        return this.max_id;
    }

    public void setMax_id(String str) {
        this.max_id = str;
    }
}
