package com.gdu.client.action;

import android.text.TextUtils;
import android.util.Pair;

import com.gdu.client.base.BaseGetAction;

import java.util.List;

/**
 * 获取MP4录像记录
 */
public class GetMp4RecordFileAction extends BaseGetAction {
   private String secret;
   private String vhost;
   private String app;
   private String stream;
   private String period;

    public GetMp4RecordFileAction(String url, String secret, String vhost, String app, String stream, String period) {
        super(url);
        this.secret = secret;
        this.vhost = vhost;
        this.app = app;
        this.stream = stream;
        this.period = period;
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
        mParams.add(new Pair("secret", secret));
        mParams.add(new Pair("vhost", vhost));
        mParams.add(new Pair("app", app));
        mParams.add(new Pair("stream", stream));
        mParams.add(new Pair("period", period));
    }
}
