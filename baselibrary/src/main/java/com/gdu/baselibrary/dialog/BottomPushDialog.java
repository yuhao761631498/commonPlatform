package com.gdu.baselibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gdu.baselibrary.R;
import com.gdu.baselibrary.base.BaseDialogFragment;
import com.gdu.baselibrary.base.BaseRVAdapter;
import com.gdu.baselibrary.widget.ColorDividerDecoration;

import java.util.List;

/**
 * 基于底部弹出的弹窗
 */
public class BottomPushDialog extends Dialog {

    public BottomPushDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
        getWindow().getAttributes().windowAnimations = R.style.dialogAnim;
    }

}
