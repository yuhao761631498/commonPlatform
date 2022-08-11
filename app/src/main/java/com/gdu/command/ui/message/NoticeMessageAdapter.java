package com.gdu.command.ui.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gdu.command.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2020-2030
 * <p>
 * 功能描述:
 * <p>
 * 创建时间: 2022/6/17 14:41
 *
 * @author yuhao
 */
public class NoticeMessageAdapter extends BaseAdapter {

    private final Context context;

    private List<String> messageList;

    public NoticeMessageAdapter(Context context, List<String> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    public void setData(List<String> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (messageList != null) {
            return messageList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.notice_message_item, parent, false);
            viewHolder.tv_message = convertView.findViewById(R.id.tv_message);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = ((ViewHolder) convertView.getTag());
        }

        viewHolder.tv_message.setText(messageList.get(position));
        return convertView;
    }


    class ViewHolder {
        private TextView tv_message;
    }
}
