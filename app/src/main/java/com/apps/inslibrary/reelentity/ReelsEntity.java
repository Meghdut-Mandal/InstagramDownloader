package com.apps.inslibrary.reelentity;

import java.io.Serializable;

public class ReelsEntity implements Serializable {

    /* renamed from: id */
    private long f201id;
    private long latestTime;
    private String userHead;
    private String userName;

    public String getUserHead() {
        return this.userHead;
    }

    public void setUserHead(String str) {
        this.userHead = str;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String str) {
        this.userName = str;
    }

    public long getId() {
        return this.f201id;
    }

    public void setId(long j) {
        this.f201id = j;
    }

    public long getLatestTime() {
        return this.latestTime;
    }

    public void setLatestTime(long j) {
        this.latestTime = j;
    }
}
