package com.meghdut.instagram.downloader.entity;


import android.os.Environment;
import android.text.TextUtils;
import java.io.Serializable;

public class SettingParams implements Serializable {
    private boolean isDirectDownload = true;
    private boolean isSituDownload = false;
    private boolean isNotification = false;
    private boolean isQuickRepost = false;
    private boolean isNotSaveHistory = true;
    private boolean isAutoCopy = true;
    private boolean isAutoFollow = true;
    private String saveFilePath = "";

    public boolean isAutoCopy() {
        return this.isAutoCopy;
    }

    public void setAutoCopy(boolean z) {
        this.isAutoCopy = z;
    }

    public boolean isAutoFollow() {
        return this.isAutoFollow;
    }

    public void setAutoFollow(boolean z) {
        this.isAutoFollow = z;
    }

    public boolean isNotification() {
        return this.isNotification;
    }

    public void setNotification(boolean z) {
        this.isNotification = z;
    }

    public boolean isDirectDownload() {
        return this.isDirectDownload;
    }

    public void setDirectDownload(boolean z) {
        this.isDirectDownload = z;
    }

    public boolean isSituDownload() {
        return this.isSituDownload;
    }

    public void setSituDownload(boolean z) {
        this.isSituDownload = z;
    }

    public boolean isQuickRepost() {
        return this.isQuickRepost;
    }

    public void setQuickRepost(boolean z) {
        this.isQuickRepost = z;
    }

    public boolean isNotSaveHistory() {
        return this.isNotSaveHistory;
    }

    public void setNotSaveHistory(boolean z) {
        this.isNotSaveHistory = z;
    }

    public String getSaveFilePath() {
        if (TextUtils.isEmpty(this.saveFilePath)) {
            return Environment.getExternalStorageDirectory() + "/Download/";
        }
        return this.saveFilePath;
    }

    public void setSaveFilePath(String str) {
        this.saveFilePath = str;
    }
}