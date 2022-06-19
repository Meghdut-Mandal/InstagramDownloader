package com.apps.inslibrary.entity.shareEntity;

import com.apps.inslibrary.entity.stroiesEntity.ImageVersions2;
import java.io.Serializable;
import java.util.List;

public class Items implements Serializable {
    private CaptionText caption;
    private List<CarouselMedia> carousel_media;
    private String code;

    private String id;
    private ImageVersions2 image_versions2;
    private int media_type;
    private ShortcodeMediaOwner user;
    private List<VideoVersions> video_versions;

    public List<CarouselMedia> getCarousel_media() {
        return this.carousel_media;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public int getMedia_type() {
        return this.media_type;
    }

    public void setMedia_type(int i) {
        this.media_type = i;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String str) {
        this.code = str;
    }

    public ShortcodeMediaOwner getUser() {
        return this.user;
    }

    public void setUser(ShortcodeMediaOwner shortcodeMediaOwner) {
        this.user = shortcodeMediaOwner;
    }

    public ImageVersions2 getImage_versions2() {
        return this.image_versions2;
    }

    public void setImage_versions2(ImageVersions2 imageVersions2) {
        this.image_versions2 = imageVersions2;
    }

    public CaptionText getCaption() {
        return this.caption;
    }

    public void setCaption(CaptionText captionText) {
        this.caption = captionText;
    }

    public List<VideoVersions> getVideo_versions() {
        return this.video_versions;
    }

    public void setVideo_versions(List<VideoVersions> list) {
        this.video_versions = list;
    }

    public void setCarousel_media(List<CarouselMedia> list) {
        this.carousel_media = list;
    }
}
