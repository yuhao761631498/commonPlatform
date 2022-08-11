package com.gdu.command.ui.cases.deal;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdu.command.R;
import com.gdu.model.cases.CommentInfo;

import java.util.List;

/**
 * 案件批示列表
 */
public class CaseCommentsAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommentInfo> mCommentInfoList;

    public CaseCommentsAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<CommentInfo> deviceInfos) {
        mCommentInfoList = deviceInfos;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        if (mCommentInfoList != null) {
            return mCommentInfoList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mCommentInfoList != null) {
            return mCommentInfoList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_case_comment, null);
            holder.mHeaderImageView = convertView.findViewById(R.id.header_imageview);
            holder.mNameTextView = convertView.findViewById(R.id.name_textview);
            holder.mTimeTextView = convertView.findViewById(R.id.time_textview);
            holder.mCommentsTextView = convertView.findViewById(R.id.comments_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (parent.getChildCount() != position) {
            return convertView;
        }
        CommentInfo commentInfo = mCommentInfoList.get(position);
        String name = commentInfo.getDeptName();
        if (TextUtils.isEmpty(name)) {
            name = "执法员" + position;
        }
        holder.mNameTextView.setText(name);
        holder.mTimeTextView.setText(commentInfo.getCheckTime());
        holder.mCommentsTextView.setText(commentInfo.getCheckComment());
        return convertView;
    }

    private class ViewHolder {
        private ImageView mHeaderImageView;
        private TextView mNameTextView;
        private TextView mTimeTextView;
        private TextView mCommentsTextView;
    }

}
