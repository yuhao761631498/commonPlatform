package com.gdu.command.ui.person;

import android.os.Handler;
import android.os.Message;

import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.ui.splash.LoginService;
import com.gdu.model.user.PersonInfoBean;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonViewModel extends ViewModel {

    private final byte FAILED = 0;
    private final byte SUCCESS = 1;

    private MutableLiveData<PersonInfoBean> mPersonInfoLiveData;

    private Handler handler;

    private PersonInfoBean personInfoBean;

    public PersonViewModel() {
        mPersonInfoLiveData = new MutableLiveData<>();
        initHandler();
        getPersonInfo();
    }

    private void initHandler() {
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case FAILED:

                        break;

                    case SUCCESS:
                        mPersonInfoLiveData.setValue(personInfoBean);
                        break;

                    default:
                        break;
                }
            }
        };
    }

    public LiveData<PersonInfoBean> getPersonInfoLiveData() {
        return mPersonInfoLiveData;
    }

    private void getPersonInfo() {
        LoginService mLoginService = RetrofitClient.getAPIService(LoginService.class);
        mLoginService.getPersonInfo().enqueue(new Callback<PersonInfoBean>() {
            @Override
            public void onResponse(Call<PersonInfoBean> call, Response<PersonInfoBean> response) {
                MyLogUtils.d("onResponse()");
                personInfoBean = response.body();
                final boolean isEmptyData = personInfoBean == null || personInfoBean.getCode() != 0 || personInfoBean.getData() == null;
                if (isEmptyData) {
                    handler.sendEmptyMessage(FAILED);
                    return;
                }
                handler.sendEmptyMessage(SUCCESS);
            }

            @Override
            public void onFailure(Call<PersonInfoBean> call, Throwable e) {
                MyLogUtils.e("获取用户信息接口失败", e);
                handler.sendEmptyMessage(FAILED);
            }
        });
    }

}