package com.gdu.command.ui.cases;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.picktime.view.OnWheelChangedListener;
import com.gdu.picktime.view.OnWheelScrollListener;
import com.wx.wheelview.widget.WheelView;

import java.util.List;

public class TypeSelectDialog extends Dialog {
    private TextView titleTv;
    private WheelView mWheelView;

    private List<TypeCodeBean.DataBean> mData;
    private WheelView.OnWheelItemClickListener mListener;
    private String mTitle;

    public TypeSelectDialog(@NonNull Context context, String title,
                            List<TypeCodeBean.DataBean> data) {
        super(context, R.style.CustomDialog);

        mTitle = title;
        mData = data;
        setContentView(R.layout.dialog_select_type);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        initView();
    }

    public void setListener(WheelView.OnWheelItemClickListener listener) {
        mListener = listener;
    }

    private void initView() {
        titleTv = findViewById(R.id.tv_title);
        mWheelView = findViewById(R.id.wv_typeContent);

        if (mTitle != null && !mTitle.isEmpty()) {
            titleTv.setText(mTitle);
        }

        setWheelListener();
    }

    protected void setWheelListener() {
        final int textColor = 0x80000000;
        final int selectColor = 0xff000000;
        TypeSelectAdapter viewAdapter = new TypeSelectAdapter(getContext());
        mWheelView.setSkin(WheelView.Skin.None);
        mWheelView.setWheelAdapter(viewAdapter);
        mWheelView.setWheelData(mData);
        mWheelView.setWheelSize(3);
        mWheelView.setWheelClickable(true);
        mWheelView.setOnWheelItemClickListener((WheelView.OnWheelItemClickListener)
                (position, o) -> {
            if (mListener != null) {
                mListener.onItemClick(position, o);
            }
        });
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.backgroundColor = Color.WHITE;
        style.textColor = textColor;
        style.selectedTextColor = selectColor;
        style.holoBorderColor = 0xFFE7E7E7;
        style.selectedTextSize = 16;
        style.textSize = 16;
        mWheelView.setStyle(style);
    }

}
