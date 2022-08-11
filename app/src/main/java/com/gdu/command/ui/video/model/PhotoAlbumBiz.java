package com.gdu.command.ui.video.model;

import android.media.MediaMetadataRetriever;

import com.gdu.model.base.MultiMediaItemBean;
import com.gdu.model.config.StorageConfig;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class PhotoAlbumBiz {

    private SimpleDateFormat simpleDateFormat;

    public PhotoAlbumBiz() {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * <ul>
     * <p>获取本地文件夹下 的 图片文件 列表</p>
     * <p>已经排序 完成</p>
     * </ul>
     */
    public ArrayList<MultiMediaItemBean> getImageListAndSort() {
        ArrayList<MultiMediaItemBean> fileList = new ArrayList<MultiMediaItemBean>();//将需要的子文件信息存入到FileInfo里面
        File parentFile = new File(StorageConfig.OUT_IMAGE_PATH);
        if (parentFile.exists()) {
            File[] files = parentFile.listFiles(fileFilter);//通过fileFileter过滤器来获取parentFile路径下的想要类型的子文件
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    MultiMediaItemBean fileInfo = new MultiMediaItemBean();
                    fileInfo.name = file.getName();
                    fileInfo.path = file.getPath();
                    fileInfo.coverPath = file.getPath();
                    fileInfo.lastModified = file.lastModified();
                    if (fileInfo.name.endsWith(".mp4")) {
                        fileInfo.type = fileInfo.MEDIA;
                        fileInfo.duration = getMp4Legnth(file.getPath());
                    } else {
                        fileInfo.type = fileInfo.IMAGE;
                    }
                    fileList.add(fileInfo);
                }
            }
        }

//        File parentFileTransrint = new File(StorageConfig.BaseDirectory + "/" + StorageConfig.ImageTempFileName);
//        if (parentFileTransrint.exists()) {
//            File[] fileTransPrint = parentFileTransrint.listFiles(fileFilter);//通过fileFileter过滤器来获取parentFile路径下的想要类型的子文件
//            if (fileTransPrint != null) {
//                for (int i = 0; i < fileTransPrint.length; i++) {
//                    File file1 = fileTransPrint[i];
//                    MultiMediaItemBean fileInfo = new MultiMediaItemBean();
//                    fileInfo.name = file1.getName();
//                    fileInfo.path = file1.getPath();
//                    fileInfo.coverPath = file1.getPath();
//                    fileInfo.lastModified = file1.lastModified();
//                    fileInfo.type = fileInfo.IMAGE;
//                    fileList.add(fileInfo);
//                }
//            }
//        }

        Collections.sort(fileList, new FileComparator());//通过重写Comparator的实现类FileComparator来实现按文件创建时间排序。
        int i = 0;
        long tempTime = 0;
        Calendar calendar = Calendar.getInstance();
        while (fileList.size() - 1 >= i) {
            MultiMediaItemBean bean = fileList.get(i);
            if (i == 0 || bean.lastModified < tempTime) {
                calendar.clear();
                calendar.setTimeInMillis(bean.lastModified);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                calendar.clear();
                calendar.set(year, month, day, 0, 0);
                tempTime = calendar.getTimeInMillis();

                MultiMediaItemBean bean1 = new MultiMediaItemBean();
                bean1.lastModified = tempTime;
                bean1.path = null;
                bean1.name = "";

                fileList.add(i, bean1);
                i += 2;
            } else
                i++;
        }
//        RonLog.LogE("查找的个数：" + fileList.size());
        return fileList;
    }

    public FileFilter fileFilter = new FileFilter() {
        public boolean accept(File file) {
            String tmp = file.getName().toLowerCase();
            if (tmp.endsWith(".jpg") || tmp.endsWith(".png") || tmp.endsWith(".jpeg") || tmp.endsWith(".mp4")) {
                return true;
            }
            return false;
        }
    };

    public class FileComparator implements Comparator<MultiMediaItemBean> {
        public int compare(MultiMediaItemBean file1, MultiMediaItemBean file2) {
            if (file1.lastModified > file2.lastModified) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    /**
     * <p>MP4的时长</p>
     *
     * @param path
     * @return
     * @throws IllegalStateException 有些Mp4连 视频的长度，获取不到
     */
    private int getMp4Legnth(String path) {
        String videoDuration = "-1";
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(path);
            videoDuration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        } catch (Exception e) {
            e.printStackTrace();
            videoDuration = "-1";
        }
        return Integer.parseInt(videoDuration);
    }


}
