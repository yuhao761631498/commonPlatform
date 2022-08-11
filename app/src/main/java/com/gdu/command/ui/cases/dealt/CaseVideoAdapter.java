package com.gdu.command.ui.cases.dealt;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.gdu.command.R;

import java.security.MessageDigest;
import java.util.List;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;

/**
 * 案件取证视频列表
 */
public class CaseVideoAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mCasePictureList;

    public CaseVideoAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<String> deviceInfos) {
        mCasePictureList = deviceInfos;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        if (mCasePictureList != null) {
            return mCasePictureList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mCasePictureList != null) {
            return mCasePictureList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_case_video, null);
            holder.mCaseImageView = convertView.findViewById(R.id.case_imageview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (parent.getChildCount() != position) {
            return convertView;
        }
        String casePath = mCasePictureList.get(position);
        String path = casePath;
        if (!casePath.contains("http:")) {
            path = "file://" + casePath;
        }
//        MyImageLoadUtils.loadImage(mContext, path, 0, holder.mCaseImageView);
        loadCover(holder.mCaseImageView, path);
        return convertView;
    }

    private class ViewHolder {
        private ImageView mCaseImageView;
    }

    /**
     * 加载第四秒的帧数作为封面
     *  url就是视频的地址
     */
    public void loadCover(ImageView imageView, String url) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        RequestOptions requestOptions = RequestOptions.frameOf(1 * 1000 * 1000);
        requestOptions.set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST);
        requestOptions.transform(new BitmapTransformation() {
            @Override
            protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                return toTransform;
            }

            @Override
            public void updateDiskCacheKey(MessageDigest messageDigest) {
                try {
                    messageDigest.update((mContext.getPackageName() + "RotateTransform").getBytes("utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Glide.with(mContext).load(url).apply(requestOptions).into(imageView);
    }

}
