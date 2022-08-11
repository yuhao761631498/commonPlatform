package com.gdu.command.ui.cases.deal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.donkingliang.imageselector.utils.VersionUtils;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.command.R;

import java.util.List;

/**
 * 案件取证列表
 */
public class CasePictureAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mCasePictureList;
    private boolean isAndroidQ = VersionUtils.isAndroidQ();

    public CasePictureAdapter(Context context) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_case_picture, null);
            holder.mCaseImageView = convertView.findViewById(R.id.case_imageview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (parent.getChildCount() != position) {
            return convertView;
        }
        String casePath = mCasePictureList.get(position);
        System.out.println("test getView " + position + " " + casePath);
        if (casePath.equals("add")) {
            holder.mCaseImageView.setImageResource(R.mipmap.icon_add_picture);
        } else {
            String path = casePath;
            if (!casePath.contains("http:")) {
                path = "file://" + casePath;
            }
            MyImageLoadUtils.loadImage(mContext, path, 0, holder.mCaseImageView);
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView mCaseImageView;
    }

}
