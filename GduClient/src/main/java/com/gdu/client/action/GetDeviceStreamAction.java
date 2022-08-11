package com.gdu.client.action;

import android.text.TextUtils;
import android.util.Pair;

import com.gdu.client.base.BasePostAction;

import java.util.List;

/**
 * 获取设备推流地址
 */
public class GetDeviceStreamAction extends BasePostAction {

    private String deviceInfoListJsonStr;

    public GetDeviceStreamAction(String url, String baseUrl, String deviceInfoListJsonStr) {
        super(url, baseUrl);
        this.deviceInfoListJsonStr = deviceInfoListJsonStr;
    }

    @Override
    protected void doResponse(String response) throws Exception {
        if (!TextUtils.isEmpty(response)) {
            setError(0);
            setData(response);
            System.out.println("test response " + response.toString());
        }
    }

    @Override
    public void setParam(List<Pair> params) {
        mParams.add(new Pair("deviceInfoListJsonStr", deviceInfoListJsonStr));
    }
}
