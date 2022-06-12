package com.apps.inslibrary.entity.stroiesEntity;

import java.io.Serializable;
import java.util.List;

public class ReelsMediaItems implements Serializable {
    private String code;

    /* renamed from: id */
    private String f189id;
    private ImageVersions2 image_versions2;
    private int media_type;

    /* renamed from: pk */
    private String f190pk;
    private List<StoryFeedMedia> story_feed_media;
    private long taken_at;
    private double video_duration;
    private List<VideoVersions> video_versions;

    public double getVideo_duration() {
        return this.video_duration;
    }

    public void setVideo_duration(double d) {
        this.video_duration = d;
    }

    public long getTaken_at() {
        return this.taken_at;
    }

    public void setTaken_at(long j) {
        this.taken_at = j;
    }

    public String getPk() {
        return this.f190pk;
    }

    public void setPk(String str) {
        this.f190pk = str;
    }

    public String getId() {
        return this.f189id;
    }

    public void setId(String str) {
        this.f189id = str;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String str) {
        this.code = str;
    }

    public int getMedia_type() {
        return this.media_type;
    }

    public void setMedia_type(int i) {
        this.media_type = i;
    }

    public ImageVersions2 getImage_versions2() {
        return this.image_versions2;
    }

    public void setImage_versions2(ImageVersions2 imageVersions2) {
        this.image_versions2 = imageVersions2;
    }

    public List<VideoVersions> getVideo_versions() {
        return this.video_versions;
    }

    public void setVideo_versions(List<VideoVersions> list) {
        this.video_versions = list;
    }

    public List<StoryFeedMedia> getStory_feed_media() {
        return this.story_feed_media;
    }

    public void setStory_feed_media(List<StoryFeedMedia> list) {
        this.story_feed_media = list;
    }
}
