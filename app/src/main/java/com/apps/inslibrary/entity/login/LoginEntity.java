package com.apps.inslibrary.entity.login;

public class LoginEntity {
    private LoginUser user;
    private Object viewer;

    public Object getViewer() {
        return this.viewer;
    }

    public void setViewer(Object obj) {
        this.viewer = obj;
    }

    public LoginUser getUser() {
        return this.user;
    }

    public void setUser(LoginUser loginUser) {
        this.user = loginUser;
    }
}
