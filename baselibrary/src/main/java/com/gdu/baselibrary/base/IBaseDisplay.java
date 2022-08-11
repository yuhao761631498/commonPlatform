package com.gdu.baselibrary.base;

import android.content.Context;

import com.gdu.baselibrary.network.ApiException;

/**
 * 用于Activity Fragment 界面交互
 */
public interface IBaseDisplay {
    Context getContext();

    BaseActivity getBaseActivity();

    void showProgressDialog();

    void showProgressDialog(CharSequence message);

    void hideProgressDialog();

    void showError(Throwable t);

    void onApiException(ApiException e);

    void onRequestFinish();

    void changeDayNightMode(boolean isNightMode);
}
