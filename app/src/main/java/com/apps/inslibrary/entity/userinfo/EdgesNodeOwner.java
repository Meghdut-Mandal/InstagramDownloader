package com.apps.inslibrary.entity.userinfo;

import java.io.Serializable;

public class EdgesNodeOwner implements Serializable {

    private String id;
    private String username;

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String str) {
        this.username = str;
    }
}
