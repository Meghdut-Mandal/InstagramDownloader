package com.apps.inslibrary.entity.stroiesEntity;

import java.io.Serializable;
import java.util.List;

public class ReelsMediaResult implements Serializable {
    private List<ReelsMedia> reels_media;
    private String status;

    public List<ReelsMedia> getReels_media() {
        return this.reels_media;
    }

    public void setReels_media(List<ReelsMedia> list) {
        this.reels_media = list;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String str) {
        this.status = str;
    }
}
