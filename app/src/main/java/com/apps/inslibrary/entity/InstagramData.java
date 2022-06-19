package com.apps.inslibrary.entity;

import com.apps.inslibrary.InstagramRes;
import java.io.Serializable;
import java.util.List;

public class InstagramData implements Serializable {
    private String coverUrl;
    private String describe;

    private String id;
    private List<InstagramRes> instagramRes;
    private InstagramUser instagramUser;
    private boolean isCheck;
    private boolean isCollection;
    private boolean isVideo;
    private String shareUrl;
    private String shortcode;
    private String viewUrl;

    public boolean isCollection() {
        return this.isCollection;
    }

    public void setCollection(boolean z) {
        this.isCollection = z;
    }

    public String getShortcode() {
        return this.shortcode;
    }

    public void setShortcode(String str) {
        this.shortcode = str;
    }

    public String getShareUrl() {
        return this.shareUrl;
    }

    public void setShareUrl(String str) {
        this.shareUrl = str;
    }

    public String getViewUrl() {
        return this.viewUrl;
    }

    public void setViewUrl(String str) {
        this.viewUrl = str;
    }

    public boolean isCheck() {
        return this.isCheck;
    }

    public void setCheck(boolean z) {
        this.isCheck = z;
    }

    public boolean isVideo() {
        return this.isVideo;
    }

    public void setVideo(boolean z) {
        this.isVideo = z;
    }

    public String getCoverUrl() {
        return this.coverUrl;
    }

    public void setCoverUrl(String str) {
        this.coverUrl = str;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public InstagramUser getInstagramUser() {
        return this.instagramUser;
    }

    public void setInstagramUser(InstagramUser instagramUser2) {
        this.instagramUser = instagramUser2;
    }

    public String getDescribe() {
        return this.describe;
    }

    public void setDescribe(String str) {
        this.describe = str;
    }

    public List<InstagramRes> getInstagramRes() {
        return this.instagramRes;
    }

    public void setInstagramRes(List<InstagramRes> list) {
        this.instagramRes = list;
    }
}
