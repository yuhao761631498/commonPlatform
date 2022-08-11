package com.gdu.command.ui.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gdu.baselibrary.utils.ScreenUtil;
import com.gdu.command.R;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;

public class SelectGenderDialog extends Dialog {

    private Context mContext;
    private TextView tv_man;
    private TextView tv_woman;
    private OnSelectGenderListener onSelectGenderListener;
    private String gender;

    public SelectGenderDialog(Context context, String sex) {
        super(context, R.style.DialogThemeBackgroundDim);
        this.mContext = context;
        this.gender = sex;
        setCanceledOnTouchOutside(true);
        selectGender();
    }

    public void setOnDialogConfirmListener(OnSelectGenderListener listener) {
        this.onSelectGenderListener = listener;
    }

    private void selectGender() {
        View view = getLayoutInflater().inflate(R.layout.item_select_gender_dialog_layout, null);
        setContentView(view);

        tv_man = view.findViewById(R.id.tv_man);
        tv_woman = view.findViewById(R.id.tv_woman);

        if(!TextUtils.isEmpty(gender)) {
            tv_man.setSelected(gender.equals("男"));
            tv_woman.setSelected(gender.equals("女"));
        }

        tv_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_man.setSelected(true);
                tv_woman.setSelected(false);
                if (onSelectGenderListener != null) {
                    onSelectGenderListener.OnClick("男");
                }
                dismiss();
            }
        });

        tv_woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_woman.setSelected(true);
                tv_man.setSelected(false);
                if (onSelectGenderListener != null) {
                    onSelectGenderListener.OnClick("女");
                }
                dismiss();
            }
        });

        setDialogStyle();
    }

    public interface OnSelectGenderListener {
        void OnClick(String gender);
    }

    /**
     * 设置弹框样式
     */
    private void setDialogStyle() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = ScreenUtil.getScreenWidth(mContext) * 3 /4;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.gravity = Gravity.CENTER;
            window.setAttributes(layoutParams);
        }
    }
}
