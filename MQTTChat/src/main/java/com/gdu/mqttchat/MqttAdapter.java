package com.gdu.mqttchat;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;

/**
 * Created by fc on 2018/12/18.
 */

public class MqttAdapter extends RecyclerView.Adapter<MqttAdapter.ViewHolder> {

    private Context mContext;
    private List<MqttMessage> messageList;
    private int ITEM_MESSAGE_ME = 0;
    private int ITEM_MESSAGE_OTHER = 1;
    private String mUserName;
    private OnMessageListener mOnMessageListener;

    public MqttAdapter(Context context, List<MqttMessage> list) {
        mContext = context;
        messageList = list;
    }

    public void setOnMessageListener(OnMessageListener onMessageListener){
        mOnMessageListener = onMessageListener;
    }

    public void setUserName(String userName){
        mUserName = userName;
    }

    public void setList(List<MqttMessage> list) {
        messageList = list;
        sortMessage();
        notifyDataSetChanged();
    }

    private void sortMessage(){
        Collections.sort(messageList, new Comparator<MqttMessage>() {
            @Override
            public int compare(MqttMessage t1, MqttMessage t2) {
                long time1 = Long.parseLong(t1.getSendTime());
                long time2 = Long.parseLong(t2.getSendTime());
                if (time1 > time2) {
                    return 1;
                } else if(time1 < time2){
                    return -1;
                }
                return 0;
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_MESSAGE_ME) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_message_me, null, false);
            return new ViewHolder(view);
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_message_other, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MqttMessage message = messageList.get(position);
        long time = Long.parseLong(message.getSendTime());
        String times = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(time));
        holder.timeTV.setText(times);
        holder.userTV.setText(message.getUsername());
        holder.contentTV.setVisibility(View.GONE);
        holder.contentIV.setVisibility(View.GONE);
        holder.videoPlayIV.setVisibility(View.GONE);
        if (message.getType().equals("txt")) {
            holder.contentTV.setVisibility(View.VISIBLE);
            holder.contentTV.setText(message.getMsg());
        } else if(message.getType().equals("img")){
            holder.contentIV.setVisibility(View.VISIBLE);
            MyImageLoadUtils.loadImage(mContext, message.getMsg(), 0, holder.contentIV);
        } else if(message.getType().equals("video")){
            holder.videoPlayIV.setVisibility(View.VISIBLE);
            holder.contentIV.setVisibility(View.VISIBLE);
            loadCover(holder.contentIV, message.getMsg());
        }
        holder.imageLayout.setOnClickListener(new OnMessageClickListener(message));
    }

    @Override
    public int getItemCount() {
        if (messageList != null) {
            return messageList.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        MqttMessage message = messageList.get(position);
        if (mUserName.equals(message.getUsername())) {
            return ITEM_MESSAGE_ME;
        }
        return ITEM_MESSAGE_OTHER;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View contentView;
        TextView userTV;
        ImageView contentIV;
        ImageView videoPlayIV;
        RelativeLayout imageLayout;
        TextView contentTV;
        TextView timeTV;

        public ViewHolder(View itemView) {
            super(itemView);
            contentView = itemView;
            userTV = contentView.findViewById(R.id.message_user);
            contentIV = contentView.findViewById(R.id.message_image);
            videoPlayIV = contentView.findViewById(R.id.video_play_imageview);
            imageLayout = contentView.findViewById(R.id.image_layout);
            contentTV = contentView.findViewById(R.id.message_text);
            timeTV = contentView.findViewById(R.id.message_time);
        }
    }

    public class OnMessageClickListener implements View.OnClickListener{

        MqttMessage message;
        public OnMessageClickListener(MqttMessage message){
            this.message = message;
        }

        @Override
        public void onClick(View view) {
            mOnMessageListener.onMessageClick(message);
        }
    }

    public interface OnMessageListener{
        void onMessageClick(MqttMessage mqttMessage);
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
