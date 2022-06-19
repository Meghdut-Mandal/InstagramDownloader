package com.apps.inslibrary;

import java.io.Serializable;

public class InstagramRes implements Serializable {
    private String display_url;

    private String id;
    private boolean is_video;
    private String saveFile;
    private String shortcode;
    private String video_url;
    private String viewUrl;

    public InstagramRes() {
    }

    public InstagramRes(String str, boolean z, String str2, String str3) {
        this.id = str;
        this.is_video = z;
        this.shortcode = str2;
        if (z) {
            this.video_url = str3;
        } else {
            this.display_url = str3;
        }
    }

    public String getSaveFile() {
        return this.saveFile;
    }

    public void setSaveFile(String str) {
        this.saveFile = str;
    }

    public String getViewUrl() {
        return this.viewUrl;
    }

    public void setViewUrl(String str) {
        this.viewUrl = str;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getShortcode() {
        return this.shortcode;
    }

    public void setShortcode(String str) {
        this.shortcode = str;
    }

    public String getDisplay_url() {
        return this.display_url;
    }

    public void setDisplay_url(String str) {
        this.display_url = str;
    }

    public boolean isIs_video() {
        return this.is_video;
    }

    public void setIs_video(boolean z) {
        this.is_video = z;
    }

    public String getVideo_url() {
        return this.video_url;
    }

    public void setVideo_url(String str) {
        this.video_url = str;
    }
}
