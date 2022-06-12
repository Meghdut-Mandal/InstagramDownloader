package com.apps.inslibrary.entity.shareEntity;

import com.apps.inslibrary.entity.stroiesEntity.ImageVersions2;
import java.io.Serializable;

public class CarouselMedia implements Serializable {

    /* renamed from: id */
    private String f178id;
    private ImageVersions2 image_versions2;
    private int media_type;

    public String getId() {
        return this.f178id;
    }

    public void setId(String str) {
        this.f178id = str;
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
}
