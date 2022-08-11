package com.gdu.command.ui.patrol.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.gdu.app.GduCPApplication;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.command.R;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;

/**
 * Created by Teprinciple on 2016/8/23.
 * 地图上自定义的infowindow的适配器
 */
public class InfoWinAdapter implements AMap.InfoWindowAdapter {
    private Context mContext = GduCPApplication.getInstance().getBaseContext();
    private LatLng latLng;
    private TextView tv_title;
    private TextView tv_content;
    private String mTitle;
    private String mContent;

    @Override
    public View getInfoWindow(Marker marker) {
        initData(marker);
        View view = initView();
        return view;
    }
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private void initData(Marker marker) {
        latLng = marker.getPosition();
        mContent = marker.getSnippet();
        mTitle = marker.getTitle();
    }

    @NonNull
    private View initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_infowindow, null);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_title.setText(mTitle);
        tv_title.setVisibility(CommonUtils.isEmptyString(mTitle) ? View.GONE : View.VISIBLE);
        tv_content.setText(mContent);
        tv_content.setVisibility(CommonUtils.isEmptyString(mContent) ? View.GONE : View.VISIBLE);
        return view;
    }

}

