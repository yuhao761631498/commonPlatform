package com.gdu.client.base;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import java.io.IOException;

/**
 * Created by zhilaizhang on 17/9/29.
 */

public abstract class BaseGetAction extends BaseAction {

    public BaseGetAction(String url) {
        super(url);
    }

    @Override
    protected void initURLConnection() throws IOException {
        sURL = linkUrl();
        Log.d("test", "test url: " + sURL);
        openConnection();
        mHttpURLConnection.setConnectTimeout(READ_TIMEOUT);
        mHttpURLConnection.setRequestMethod("GET");
        if(!(TextUtils.isEmpty(mHeaderKey) || TextUtils.isEmpty(mHeaderValue))){
            mHttpURLConnection.setRequestProperty(mHeaderKey, mHeaderValue);
        }
    }

    /**
     * 拼接get url请求链接
     *
     * @return
     */
    private String linkUrl() {
        if (mParams == null || mParams.size() == 0) {
            return sURL;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(sb);
        sb.append(sURL);
        sb.append("?");
        int len = mParams.size();
        for (int i = 0; i < len; i++) {
            Pair nameValuePair = mParams.get(i);
            sb.append(nameValuePair.first);
            sb.append("=");
            sb.append(nameValuePair.second);
            if (i != len - 1) {
                sb.append("&");
            }
        }
        return sb.toString();
    }

    @Override
    protected void doException(Exception exception, int errCode) {
        setError(errCode);
        setData(null);
    }
}
