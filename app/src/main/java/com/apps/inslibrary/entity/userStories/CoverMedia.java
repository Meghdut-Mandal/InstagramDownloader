package com.apps.inslibrary.entity.userStories;

public class CoverMedia {
    private CroppedImageVersion cropped_image_version;
    private String media_id;

    public CroppedImageVersion getCropped_image_version() {
        return this.cropped_image_version;
    }

    public void setCropped_image_version(CroppedImageVersion croppedImageVersion) {
        this.cropped_image_version = croppedImageVersion;
    }

    public String getMedia_id() {
        return this.media_id;
    }

    public void setMedia_id(String str) {
        this.media_id = str;
    }
}
