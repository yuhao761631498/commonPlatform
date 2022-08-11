package com.gdu.client.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.util.Pair;

import com.gdu.client.handler.ActionHandler;
import com.gdu.client.handler.AsyncActionHandler;
import com.gdu.client.util.HttpCacheDao;
import com.google.gson.Gson;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by zhilaizhang on 17/9/29.
 */

public abstract class BaseAction extends Action {
    protected Context mContext;
    protected ActionHandler mActionHandler;
    protected AsyncActionHandler mAsyncActionHandler;
    protected boolean isAsync;
    protected boolean isNeedCache;

    protected Gson mGson;

    protected HttpURLConnection mHttpURLConnection;
    protected HttpCacheDao mHttpCacheDao;

    protected List<Pair> mParams;

    protected String sURL;

    protected String mHeaderKey;
    protected String mHeaderValue;

    public BaseAction(String url) {
        sURL = url;
        mGson = new Gson();
        mParams = new ArrayList<>();
    }

    /**
     * 添加头部文件
     * @param key
     * @param value
     */
    public void setHeader(String key, String value){
        mHeaderKey = key;
        mHeaderValue = value;
    }

    public void execute(boolean isAsync, ActionHandler actionHandler) {
        this.isAsync = isAsync;
        mActionHandler = actionHandler;
        actionHandler.doActionStart();
        setParam(mParams);
        if (mContext != null) {
            mHttpCacheDao = new HttpCacheDao(mContext);
        }
        if (isAsync) {
            executeAsync();
        } else {
            executeSync();
        }
    }

    /**
     * 同步请求
     */
    private void executeSync() {
        getConnection();
    }

    /**
     * 异步请求
     */
    private void executeAsync() {
        mAsyncActionHandler = new AsyncActionHandler();
        mAsyncActionHandler.setActionHandler(mActionHandler);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getConnection();
            }
        }).start();
    }

    public void getConnection() {
        try {
            initURLConnection();
            String response = getResponse();
            System.out.println("test response " + response);
            doResponse(response);
            handleResponse();
        } catch (ConnectTimeoutException e) {
            handleException(e, Action.STATUS_CONNECT_TIME_OUT);
        } catch (SocketTimeoutException e) {
            handleException(e, Action.STATUS_SOCKET_TIME_OUT);
        } catch (IOException e) {
            handleException(e, Action.STATUS_IO_ERROR);
        } catch (Exception e) {
            handleException(e, Action.STATUS_FAILED);
        }
    }

    public void openConnection() throws IOException {
        System.out.println("test url " + sURL);
        mHttpURLConnection = (HttpURLConnection) new URL(sURL).openConnection();
    }

    protected void handleException(Exception exception, int errCode) {
        Log.d("BaseAction", "requestError = " + exception.getMessage());
        doException(exception, errCode);
        handleResponse();
    }

    private void handleResponse() {
        if (isAsync) {
            Message message = new Message();
            Bundle data = new Bundle();
            data.putInt(AsyncActionHandler.ACTION_STATUS, getError());
            data.putSerializable(AsyncActionHandler.ACTION_DATA, getData());
            message.setData(data);
            mAsyncActionHandler.sendMessage(message);
        } else {
            mActionHandler.doActionRawData(getData());
            mActionHandler.doActionResponse(getError(), getData());
        }
    }


    public String getResponse() throws IOException {
        int respCode = 0;
        String result = null;
        respCode = mHttpURLConnection.getResponseCode();
        if (respCode == HttpURLConnection.HTTP_OK) {
            result = readStream(mHttpURLConnection.getInputStream());
            if (isNeedCache) {
                mHttpCacheDao.saveHttpCache(sURL, new HashMap<String, String>(), result);
            }
        }
        return result;
    }

    /**
     * <p>读取流里面的内容</p>
     *
     * @param is
     * @return
     * @throws IOException
     */
    private static String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (is != null) {
            byte[] buffer = new byte[1024 * 10];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, len));
            }
            is.close();
            is = null;
        }
        return sb.toString();
    }

    protected abstract void doResponse(String response) throws Exception;

    protected abstract void doException(Exception exception, int errCode);

    public abstract void setParam(List<Pair> params);

    protected abstract void initURLConnection() throws Exception;
}
