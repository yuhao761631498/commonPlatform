package com.gdu.client.action;

import android.text.TextUtils;
import android.util.Pair;

import com.gdu.client.base.BasePostAction;
import com.gdu.model.user.PersonInfoBean;

import java.util.List;

/**
 * Created by zhangzhilai on 2017/11/17.
 * 获取用户信息接口
 */

public class GetWeatherAction extends BasePostAction {

    public GetWeatherAction(String url){
        super(url, "");
    }

    @Override
    protected void doResponse(String response) throws Exception {
        if (!TextUtils.isEmpty(response)) {
            setError(0);
            setData(response);
        }
    }

    @Override
    public void setParam(List<Pair> params) {
    }
}
