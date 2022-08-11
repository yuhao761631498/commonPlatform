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
 * 记录处置
 */
public class RecordDealtAdapter extends BaseAdapter {

    private Context mContext;
    private List<Map<String, String>> mDealtList;

    public RecordDealtAdapter(Context context){
        mContext = context;
    }

    public void setData(List<Map<String, String>> commentList){
        mDealtList = commentList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mDealtList != null) {
           return mDealtList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return mDealtList.get(i);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_record_dealt, null);
            holder.mDealtTimeTextView = convertView.findViewById(R.id.dealt_time_textview);
            holder.mDealtNameTextView = convertView.findViewById(R.id.dealt_name_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Map<String,String> commentMap = mDealtList.get(position);

//        String comments = commentMap.get("checkComment");
        String time = commentMap.get("createTime");
        String name = commentMap.get("employeeName");
        String deptName = commentMap.get("deptName");
        holder.mDealtTimeTextView.setText(time);
        holder.mDealtNameTextView.setText(name + " " + deptName);
//        holder.mUsedTimeTextView.setText(comments);
        return convertView;
    }

    private class ViewHolder {
        private TextView mDealtNameTextView;
        private TextView mDealtTimeTextView;
    }
}
