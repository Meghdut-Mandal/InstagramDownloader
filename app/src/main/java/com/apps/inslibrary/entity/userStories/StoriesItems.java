package com.apps.inslibrary.entity.userStories;

import com.apps.inslibrary.entity.stroiesEntity.ImageVersions2;
import com.apps.inslibrary.entity.stroiesEntity.VideoVersions;
import java.util.List;

public class StoriesItems {

    /* renamed from: id */
    private String f194id;
    private ImageVersions2 image_versions2;
    private int media_type;

    /* renamed from: pk */
    private String f195pk;
    private String taken_at;
    private double video_duration;
    private List<VideoVersions> video_versions;

    public String getTaken_at() {
        return this.taken_at;
    }

    public void setTaken_at(String str) {
        this.taken_at = str;
    }

    public String getPk() {
        return this.f195pk;
    }

    public void setPk(String str) {
        this.f195pk = str;
    }

    public String getId() {
        return this.f194id;
    }

    public void setId(String str) {
        this.f194id = str;
    }

    public int getMedia_type() {
        return this.media_type;
    }

    public void setMedia_type(int i) {
        this.media_type = i;
    }

    public double getVideo_duration() {
        return this.video_duration;
    }

    public void setVideo_duration(double d) {
        this.video_duration = d;
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
}
