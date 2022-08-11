package com.gdu.model.base;

/**
 * 多媒体类型
 */
public enum  MediaType {
    IMAGE(1,"img"), VIDEO(2, "video"),VOICE(3, "voice");

    int key;
    String value;
    MediaType(int i, String image) {
        key = i;
        value = image;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
