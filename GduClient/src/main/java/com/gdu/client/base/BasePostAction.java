package com.gdu.client.base;

import android.text.TextUtils;
import android.util.Pair;

import java.io.OutputStream;


/**
 * Created by zhilaizhang on 17/9/29.
 */

public abstract class BasePostAction extends BaseAction {

    private String mContentType;

    public BasePostAction(String url) {
        super(url);
        this.sURL = BASE_URL + url;
    }

    public BasePostAction(String url, String baseUrl){
        super(url);
        this.sURL = baseUrl + url;
    }


    /**
     * 设置类型
     * @param contentType
     */
    public void setContentType(String contentType){
        mContentType = contentType;
    }


    @Override
    protected void initURLConnection() throws Exception {
        String params = initBody();
        System.out.println("test post body " + params);
        openConnection();
        mHttpURLConnection.setRequestMethod("POST");
        mHttpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
        mHttpURLConnection.setReadTimeout(READ_TIMEOUT);
        mHttpURLConnection.setDoOutput(true);
        mHttpURLConnection.setDoInput(true);
        if(!(TextUtils.isEmpty(mHeaderKey) || TextUtils.isEmpty(mHeaderValue))){
            mHttpURLConnection.setRequestProperty(mHeaderKey, mHeaderValue);
        }
        String contentType;
        if (TextUtils.isEmpty(mContentType)) {
            contentType = "application/x-www-form-urlencoded";
        } else {
            contentType = mContentType;
        }
        mHttpURLConnection.setRequestProperty("Content-Type", contentType);
        mHttpURLConnection.setRequestProperty("Content-Length", String.valueOf(params.getBytes().length));
        OutputStream os = mHttpURLConnection.getOutputStream();
        os.write(params.getBytes());
        os.flush();
        os.close();
    }


    protected String initBody() {
        if (mParams != null) {
            StringBuilder builder = new StringBuilder();
            int length = mParams.size();
            for (int i = 0; i < length; i++) {
                Pair param = mParams.get(i);
                builder.append(param.first);
                builder.append("=").append(param.second);
                if (i != length - 1) {
                    builder.append("&");
                }
            }
            return builder.toString();
        }
        return null;
    }


    @Override
    protected void doException(Exception exception, int errCode) {
        setError(errCode);
        setData(null);
    }
}
