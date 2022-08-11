package com.gdu.command.ui.alarm.dialog;

import android.content.Context;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gdu.baselibrary.dialog.BottomPushDialog;
import com.gdu.command.R;

/**
 * 预警详情误报弹窗
 * @author wixche
 */
public class AlarmFalseAlarmDialog extends BottomPushDialog {
    private TextView cancelBtn, confirmBtn;
    private EditText et_remarkContent;
    private IFalseAlarmCallback mFalseAlarmCallback;

    public AlarmFalseAlarmDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_alarm_false_alarm);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        initView();
        initListener();
    }

    private void initView() {
        cancelBtn = findViewById(R.id.tv_cancelBtn);
        confirmBtn = findViewById(R.id.tv_confirmBtn);
        et_remarkContent = findViewById(R.id.et_remarkContent);
    }

    private void initListener() {
        cancelBtn.setOnClickListener(v -> {
            dismiss();
        });
        confirmBtn.setOnClickListener(v -> {
            final String remarkStr = et_remarkContent.getText().toString().trim();
            if (mFalseAlarmCallback != null) {
                mFalseAlarmCallback.callbackParam(remarkStr);
            }
            dismiss();
        });
    }

    public void setFalseAlarmCallback(IFalseAlarmCallback falseAlarmCallback) {
        mFalseAlarmCallback = falseAlarmCallback;
    }

    public interface IFalseAlarmCallback {
        void callbackParam(String content);
    }
}
