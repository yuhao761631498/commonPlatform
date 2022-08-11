package com.gdu.command.ui.upgrade;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdu.command.R;
import com.gdu.baselibrary.utils.CommonUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 * 应用更新弹窗.
 * @author wixche
 */
public class AppUpdateDialog extends DialogFragment {
    private String version;
    private String detail;
    private IBtnClickListenerRecall mBtnClickListenerRecall;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.DialogActivityTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_app_upgrade, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        TextView versionTV = view.findViewById(R.id.tv_upgrade_dialog_version);
        TextView detailTV = view.findViewById(R.id.tv_upgrade_dialog_detail);
        TextView talkLaterTV = view.findViewById(R.id.tv_upgrade_dialog_cancelBtn);
        TextView downloadNowTV = view.findViewById(R.id.tv_upgrade_dialog_confirmBtn);
        final String versionStr = "V" + version;
        versionTV.setText(versionStr);
        if (!CommonUtils.isEmptyString(detail)) {
            detailTV.setText(detail);
        }
        talkLaterTV.setOnClickListener(v -> mBtnClickListenerRecall.cancelBtnClickListener());
        downloadNowTV.setOnClickListener(v -> mBtnClickListenerRecall.okBtnClickListener());
    }

    public void show(String version, String detail,
                     FragmentManager fragmentManager,
                     IBtnClickListenerRecall iBtnClickListenerRecall) {
        this.version = version;
        this.detail = detail;
        this.mBtnClickListenerRecall = iBtnClickListenerRecall;
        show(fragmentManager, "AppUpdateDialogFragment");
    }

}
