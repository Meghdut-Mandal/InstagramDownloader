package com.apps.inslibrary.entity.login;

public class LoginResult {
    private LoginEntity data;
    private String status;

    public LoginEntity getData() {
        return this.data;
    }

    public void setData(LoginEntity loginEntity) {
        this.data = loginEntity;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String str) {
        this.status = str;
    }
}
