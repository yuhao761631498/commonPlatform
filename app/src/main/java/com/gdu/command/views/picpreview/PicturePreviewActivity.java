package com.gdu.command.views.picpreview;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.command.R;
import com.gdu.command.ui.photopreview.PhotoView;
import com.gyf.immersionbar.ImmersionBar;

import java.io.File;

/**
 * 照片预览界面.
 * @author wixche
 */
public class PicturePreviewActivity extends Activity {
    PhotoView previewIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).fitsSystemWindows(true).transparentBar().init();
        setContentView(R.layout.activity_pic_preview);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        previewIv = findViewById(R.id.iv_pic_preview_content);

        init();
    }

    protected void init() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            final String picPath = bundle.getString(MyConstants.SEND_PIC_PATH_KEY);
            if (picPath == null) {
                return;
            }
            if (picPath.contains("http")) {
                MyImageLoadUtils.loadImage(PicturePreviewActivity.this, picPath, 0, previewIv);
            } else {
                previewIv.setImageURI(Uri.fromFile(new File(picPath)));
            }
        }

        previewIv.setOnClickListener(v -> finish());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

}
