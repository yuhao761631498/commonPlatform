package com.gdu.utils;


import com.gdu.model.config.StorageConfig;

import java.io.File;

public class CPStringUtils {

    public static String createVideoThumbnailName(String fileName) {
        String cache = StorageConfig.BaseDirectory + "/" + StorageConfig.OUT_IMAGE_PATH + "/cache/" + fileName.replace(".mp4", ".jpg");
        File file = new File(StorageConfig.BaseDirectory + "/" + StorageConfig.OUT_IMAGE_PATH + "/cache");//防止存储视频封面的时候出错导致视频没有缩略图，当文件夹写入出错变成文件时，删除文件夹
        if (file.isFile()) {
            file.delete();
        }
        return cache;
    }

    /**
     * <p>时间长，转换为 String</p>
     *
     * @param duration
     * @return
     */
    public static String duration2Str(int duration) {
        StringBuffer stringBuffer = new StringBuffer();
        int hour = duration / 60 / 60;
        int minute = duration / 60 % 60;
        int second = duration % 60;
        if (hour > 0) {
            if (hour < 10) {
                stringBuffer.append(0).append(hour);
            } else {
                stringBuffer.append(hour);
            }

            stringBuffer.append(":");
        }

        if (minute >= 10) {
            stringBuffer.append(minute);
        } else {
            stringBuffer.append(0).append(minute);
        }

        stringBuffer.append(":");

        if (second >= 10) {
            stringBuffer.append(second);
        } else {
            stringBuffer.append(0).append(second);
        }

        return stringBuffer.toString();
    }

}
