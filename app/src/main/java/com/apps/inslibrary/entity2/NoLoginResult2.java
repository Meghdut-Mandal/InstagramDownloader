package com.apps.inslibrary.entity2;

public class NoLoginResult2 {
    private NoLoginEntity2 data;
    private String html;
    private String success;

    public String getSuccess() {
        return this.success;
    }

    public void setSuccess(String str) {
        this.success = str;
    }

    public NoLoginEntity2 getData() {
        return this.data;
    }

    public void setData(NoLoginEntity2 noLoginEntity2) {
        this.data = noLoginEntity2;
    }

    public String getHtml() {
        return this.html;
    }

    public void setHtml(String str) {
        this.html = str;
    }
}
