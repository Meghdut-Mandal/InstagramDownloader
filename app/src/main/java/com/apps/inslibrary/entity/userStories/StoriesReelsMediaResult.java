package com.apps.inslibrary.entity.userStories;

import java.util.List;

public class StoriesReelsMediaResult {
    private List<ReelsStoriesMedia> reels_media;
    private String status;

    public List<ReelsStoriesMedia> getReels_media() {
        return this.reels_media;
    }

    public void setReels_media(List<ReelsStoriesMedia> list) {
        this.reels_media = list;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String str) {
        this.status = str;
    }
}
