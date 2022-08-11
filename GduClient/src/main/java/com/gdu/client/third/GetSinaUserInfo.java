package com.gdu.client.third;

import android.util.Pair;

import com.gdu.client.base.BaseGetAction;

import java.util.List;

/**
 * Created by zhangzhilai on 2018/3/20.
 */

public class GetSinaUserInfo extends BaseGetAction {


    private String access_token;
    private String uid;

    public GetSinaUserInfo(String url, String access_token, String uid){
        super(url);
        this.access_token = access_token;
        this.uid = uid;
    }

    @Override
    protected void doResponse(String response) throws Exception {
        setData(response);
    }

    @Override
    public void setParam(List<Pair> params) {
        mParams.add(new Pair("access_token", access_token));
        mParams.add(new Pair("uid", uid));
    }
}
