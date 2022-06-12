package com.apps.inslibrary.entity2;

import java.util.List;

public class NoLoginResult {
    private String code;
    private String message;
    private List<NoLoginEntity> result;

    public String getCode() {
        return this.code;
    }

    public void setCode(String str) {
        this.code = str;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public List<NoLoginEntity> getResult() {
        return this.result;
    }

    public void setResult(List<NoLoginEntity> list) {
        this.result = list;
    }
}
