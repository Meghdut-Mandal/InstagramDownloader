package com.apps.inslibrary.entity.stories2;

import com.apps.inslibrary.entity.stroiesEntity.ImageVersions2;
import com.apps.inslibrary.entity.stroiesEntity.VideoVersions;
import java.util.List;

public class Stories2Item {
    private String code;

    /* renamed from: id */
    private String f186id;
    private ImageVersions2 image_versions2;
    private int media_type;

    /* renamed from: pk */
    private String f187pk;
    private List<VideoVersions> video_versions;

    public String getCode() {
        return this.code;
    }

    public void setCode(String str) {
        this.code = str;
    }

    public String getId() {
        return this.f186id;
    }

    public void setId(String str) {
        this.f186id = str;
    }

    public String getPk() {
        return this.f187pk;
    }

    public void setPk(String str) {
        this.f187pk = str;
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
}
