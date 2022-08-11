package com.gdu.baselibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.gdu.baselibrary.network.ApiException;
import com.gdu.baselibrary.utils.InstanceUtil;

import java.lang.reflect.ParameterizedType;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Fragment 基类  已实现以下功能
 * <p>
 * 1.显示/隐藏Loading弹框
 * 2.ButterKnife 绑定数据
 * 3.控制RxJava生命周期，防止内存泄漏
 * 4.MVP模式 参考 https://github.com/north2016/T-MVP
 * 需要时 可重写createPresenter() {@link BaseActivity#createPresenter()}  并且使用泛型 <P extends BasePresenter> 为当前Presenter实例
 */
public abstract class BaseFragment<P extends IBasePresenter> extends Fragment implements IBaseDisplay {
    /*** 通用的 用于传递数据的Key  */
    public static final String KEY_DATA = BaseActivity.KEY_DATA;
    public static final String KEY_TITLE = BaseActivity.KEY_TITLE;
    public static final String KEY_TYPE = BaseActivity.KEY_TYPE;
    public static final String KEY_LOGIN_EVENT = BaseActivity.KEY_LOGIN_EVENT;
    public static final int REQUEST_LOGIN = BaseActivity.REQUEST_LOGIN;
    public static final int REQUEST_CODE = BaseActivity.REQUEST_CODE;

    protected AppCompatActivity mActivity;
    protected Context mContext;

    private int position;
    private String title;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void onAttach(Context context) {
        mActivity = (AppCompatActivity) context;
        mContext = context;
        super.onAttach(context);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(getLayoutId(), container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPresenter();
        initialize();
    }


    /**
     * 切换日夜模式
     *
     * @param isNightMode
     */
    @Override
    public void changeDayNightMode(boolean isNightMode) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).changeDayNightMode(isNightMode);
        }
    }

    public boolean isEqualsLanguage(Locale mLanguage, Locale locale) {
        if (getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).isEqualsLanguage(mLanguage, locale);
        } else {
            return false;
        }
    }

    /**
     * 显示加载框
     */
    @Override
    public void showProgressDialog() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showProgressDialog();
        }
    }

    /**
     * 显示加载框（带文字）
     */
    @Override
    public void showProgressDialog(CharSequence message) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showProgressDialog(message);
        }
    }

    /**
     * 隐藏加载框
     */
    @Override
    public void hideProgressDialog() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).hideProgressDialog();
        }
    }

    @Override
    public void showError(Throwable throwable) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showError(throwable);
        }
    }

    @Override
    public void onApiException(ApiException e) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).onApiException(e);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_LOGIN) {
            int eventId = 0;
            if (data != null) {
                eventId = data.getIntExtra(KEY_LOGIN_EVENT, 0);
            }
            backFromLogin(eventId);//从登陆界面返回  登录成功
        }
    }

    /**
     * 登录成功 返回回调
     *
     * @param eventId 一般为点击View的id，可根据id判断接点击事件，从而继续操作流程
     */
    protected void backFromLogin(int eventId) {

    }

    @Override
    public BaseActivity getBaseActivity() {
        if (getActivity() instanceof BaseActivity) {
            return (BaseActivity) getActivity();
        }
        throw new RuntimeException("getActivity is not instanceof BaseActivity");
    }

    public void startActivity(Class<? extends Activity> cls) {
        this.startActivity(new Intent(getActivity(), cls));
    }

    public void startActivityForResult(Class<? extends Activity> cls, int request) {
        this.startActivityForResult(new Intent(getActivity(), cls), request);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestFinish() {

    }

    /*********************** MVP 参考 https://github.com/north2016/T-MVP ***************************/
    private P mPresenter;


    protected void initPresenter() {
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    /**
     * 创建Presenter 此处已重写 需要时重写即可
     *
     * @return
     */
    public P createPresenter() {
        if (this instanceof IBaseDisplay
                && this.getClass().getGenericSuperclass() instanceof ParameterizedType
                && ((ParameterizedType) (this.getClass().getGenericSuperclass())).getActualTypeArguments().length > 0) {
            Class mPresenterClass = (Class) ((ParameterizedType) (this.getClass().getGenericSuperclass()))
                    .getActualTypeArguments()[0];//获取Presenter的class
            return InstanceUtil.getInstance(mPresenterClass);
        }
        return null;
    }

    /*********************** MVP 参考 https://github.com/north2016/T-MVP ***************************/


    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initialize();


}
