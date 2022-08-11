package com.gdu.baselibrary.base;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.gdu.baselibrary.network.ApiException;
import com.gdu.baselibrary.utils.InstanceUtil;

import java.lang.reflect.ParameterizedType;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 * DialogFragment 基类
 */
public abstract class BaseDialogFragment<P extends IBasePresenter> extends DialogFragment implements IBaseDisplay {
    public final String TAG = getClass().getSimpleName();

    public <T extends View> T findViewById(@IdRes int id) {
        return getView().findViewById(id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View mView = inflater.inflate(getLayoutId(), container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initContentView();
        initPresenter();
        initialize();
    }


    protected void initContentView() {
        if (!isAdded()) {
            return;
        }
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable());
        setStyle(DialogFragment.STYLE_NO_INPUT, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
    }

    public void show(FragmentManager manager) {
        show(manager, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @LayoutRes
    public abstract int getLayoutId();

    public abstract void initialize();

    @Override
    public synchronized void showProgressDialog() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showProgressDialog();
        }
    }

    @Override
    public synchronized void showProgressDialog(CharSequence message) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showProgressDialog(message);
        }
    }


    @Override
    public synchronized void hideProgressDialog() {
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
    public void changeDayNightMode(boolean isNightMode) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).changeDayNightMode(isNightMode);
        }
    }

    @Override
    public BaseActivity getBaseActivity() {
        if (getActivity() instanceof BaseActivity) {
            return (BaseActivity) getActivity();
        }
        throw new RuntimeException("getActivity is not instanceof BaseActivity");
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(this);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(this);
        }

    }

    /**
     * 消失时触发
     *
     * @param onDismissListener
     */
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    private OnDismissListener onDismissListener;

    public interface OnDismissListener {

        void onDismiss(DialogFragment dialog);
    }

    @Override
    public void onRequestFinish() {

    }

    /******************************************* MVP **********************************************/
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

    /******************************************* MVP **********************************************/


}
