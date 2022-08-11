package com.gdu.command.ui.home.banner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gdu.command.R;
import com.gdu.mqttchat.chat.presenter.MessageBean;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

/**
 * 自定义布局，图片
 */
public class TextAdapter extends BannerAdapter<MessageBean.DataBean, TextAdapter.ViewHolder> {
    private Context mContext;

    public TextAdapter(Context context, List<MessageBean.DataBean> mDatas) {
        //设置数据，也可以调用banner提供的方法,或者自己在adapter中实现
        super(mDatas);
        mContext = context;
    }

    @Override
    public ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_notify_banner, parent,
                false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindView(ViewHolder holder, MessageBean.DataBean data, int position, int size) {
        holder.contentTv.setText(data.getCaseName());
//        holder.lockBtnTv.setVisibility(View.VISIBLE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView contentTv;
        public TextView lockBtnTv;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.contentTv = view.findViewById(R.id.tv_content);
            this.lockBtnTv = view.findViewById(R.id.tv_lockDetail);
        }
    }
}