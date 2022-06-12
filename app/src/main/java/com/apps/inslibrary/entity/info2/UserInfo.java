package com.apps.inslibrary.entity.info2;

import com.apps.inslibrary.entity.InstagramData;
import com.apps.inslibrary.entity.userinfo.PageInfo;
import java.util.List;

public class UserInfo {
    private String biography;
    private long fens_count;
    private long follow_count;
    private List<InstagramData> instagramData;
    private PageInfo pageInfo;
    private long tz_count;

    public String getBiography() {
        return this.biography;
    }

    public void setBiography(String str) {
        this.biography = str;
    }

    public long getFens_count() {
        return this.fens_count;
    }

    public void setFens_count(long j) {
        this.fens_count = j;
    }

    public long getFollow_count() {
        return this.follow_count;
    }

    public void setFollow_count(long j) {
        this.follow_count = j;
    }

    public long getTz_count() {
        return this.tz_count;
    }

    public void setTz_count(long j) {
        this.tz_count = j;
    }

    public List<InstagramData> getInstagramData() {
        return this.instagramData;
    }

    public void setInstagramData(List<InstagramData> list) {
        this.instagramData = list;
    }

    public PageInfo getPageInfo() {
        return this.pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo2) {
        this.pageInfo = pageInfo2;
    }
}
