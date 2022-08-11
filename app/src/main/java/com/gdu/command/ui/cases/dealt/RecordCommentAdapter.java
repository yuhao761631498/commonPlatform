package com.gdu.command.ui.cases.dealt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gdu.command.R;

import java.util.List;
import java.util.Map;

/**
 * 记录批示
 */
public class RecordCommentAdapter extends BaseAdapter {

    private Context mContext;
    private List<Map<String, String>> mCommentList;

    public RecordCommentAdapter(Context context){
        mContext = context;
    }

    public void setData(List<Map<String, String>> commentList){
        mCommentList = commentList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mCommentList != null) {
           return mCommentList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return mCommentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_record_comments, null);
            holder.mRecordCommentTextView = convertView.findViewById(R.id.record_comment_textview);
            holder.mRecordTimeTextView = convertView.findViewById(R.id.comments_time_textview);
            holder.mRecordNameTextView = convertView.findViewById(R.id.comments_name_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Map<String,String> commentMap = mCommentList.get(position);

        String comments = commentMap.get("checkComment");
        String time = commentMap.get("checkTime");
        String name = commentMap.get("employeeName");
        String deptName = commentMap.get("deptName");
        holder.mRecordCommentTextView.setText(comments);
        holder.mRecordTimeTextView.setText(time);
        holder.mRecordNameTextView.setText(name + " " + deptName);
        return convertView;
    }

    private class ViewHolder {
        private TextView mRecordCommentTextView;
        private TextView mRecordTimeTextView;
        private TextView mRecordNameTextView;
    }
}
