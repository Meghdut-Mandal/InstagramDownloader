package com.apps.inslibrary.entity.shareEntity;

import java.io.Serializable;

public class Graphql implements Serializable {
    private ShortcodeMedia shortcode_media;

    public ShortcodeMedia getShortcode_media() {
        return this.shortcode_media;
    }

    public void setShortcode_media(ShortcodeMedia shortcodeMedia) {
        this.shortcode_media = shortcodeMedia;
    }
}
