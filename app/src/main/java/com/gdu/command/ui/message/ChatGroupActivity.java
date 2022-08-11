package com.gdu.command.ui.message;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.command.R;
import com.gdu.model.config.GlobalVariable;
import com.gyf.immersionbar.ImmersionBar;

/**
 * 讨论组信息
 */
public class ChatGroupActivity extends BaseActivity {

    private TextView mChatGroupTextView;
    private ImageView mBackImageView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat_group;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                com.gdu.mqttchat.R.color.color_224CD0).init();
        initView();
        initListener();
    }

    private void initView() {
        mChatGroupTextView = findViewById(R.id.chat_group_name_textview);
        mBackImageView = findViewById(R.id.back_imageview);
        if (GlobalVariable.sCurrentIssueBean != null) {
            mChatGroupTextView.setText(GlobalVariable.sCurrentIssueBean.getCaseName() + "执法小组");
        }
    }

    private void initListener() {
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
