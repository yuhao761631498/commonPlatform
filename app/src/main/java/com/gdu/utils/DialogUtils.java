package com.gdu.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gdu.command.R;

public class DialogUtils {

    private Context mContext;
    public Dialog myDialog;

    public DialogUtils(Context context) {
        mContext = context;
    }

    /**
     * <ul>
     * <li>创建 下排 带2个按钮的 dialog</li>
     * </ul>
     *
     * @param title
     * @param content
     * @param cacel
     * @param ok
     * @param onClickListener
     */
    public void createDialogWith2Btn(String title, String content, String cacel, String ok, View.OnClickListener onClickListener) {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.cancel();
        }

        myDialog = new Dialog(mContext, R.style.droneSetDialog);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_layout_2btn, null);
        TextView btn_cancel = (TextView) view.findViewById(R.id.dialog_btn_cancel);
        TextView btn_ok = (TextView) view.findViewById(R.id.dialog_btn_sure);

        TextView tv_title = (TextView) view.findViewById(R.id.dialog_tv_title);
        tv_title.setText(title);

        TextView tv_content = (TextView) view.findViewById(R.id.dialog_tv_content);
        tv_content.setText(content);

        if (!TextUtils.isEmpty(cacel)) {
            btn_cancel.setText(cacel);
        }

        if (!TextUtils.isEmpty(ok)) {
            btn_ok.setText(ok);
        }

        btn_cancel.setOnClickListener(onClickListener);
        btn_ok.setOnClickListener(onClickListener);
        myDialog.setContentView(view);
        myDialog.show();
    }

    /**
     * <p>取消Dialog</p>
     */
    public void cancelDialog() {
        if (myDialog != null && myDialog.isShowing()) {
            myDialog.cancel();
        }
    }

}
