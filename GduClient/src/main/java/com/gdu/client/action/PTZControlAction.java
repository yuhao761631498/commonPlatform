package com.gdu.client.action;


import android.text.TextUtils;
import android.util.Pair;

import com.gdu.client.base.BasePostAction;

import java.util.List;
import java.util.Map;

public class PTZControlAction extends BasePostAction {

    private Map<String, String> mParamMap;

    public PTZControlAction(String url, String baseUrl, Map<String, String> params) {
        super(url, baseUrl);
        mParamMap = params;
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
        for (String key : mParamMap.keySet()) {
            mParams.add(new Pair(key, mParamMap.get(key)));
        }
    }
}
