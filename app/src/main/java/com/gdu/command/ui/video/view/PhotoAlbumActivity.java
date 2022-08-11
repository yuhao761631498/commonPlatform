package com.gdu.command.ui.video.view;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.gdu.baselibrary.base.RefreshActivity;
import com.gdu.command.R;
import com.gdu.command.ui.video.adapter.PhotoAlbumAdapter;
import com.gdu.command.ui.video.presenter.PhotoAlbumPresenter;
import com.gdu.model.base.MultiMediaItemBean;
import com.gdu.utils.DialogUtils;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 相册
 */
public class PhotoAlbumActivity extends RefreshActivity<PhotoAlbumPresenter>
        implements View.OnClickListener, IPhotoAlbumView {

    private GridLayoutManager mGridLayoutManager;
    private PhotoAlbumAdapter mPhotoAlbumAdapter;
    private View.OnClickListener mOnClickListener;
    private DialogUtils mDialogUtils;

    private ImageView mBackImageView;
    private ImageView mSelectImageView;
    private ImageView mDeleteImageView;

    private int hadSelectNum;
    private boolean isCheckAll;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_album;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true)
                .statusBarColor(R.color.color_224CD0).init();
        initView();
        initListener();
        initData();
    }

    private void initData() {
        isCheckAll = false;
        mDialogUtils = new DialogUtils(this);
    }

    private void initListener() {
        mBackImageView.setOnClickListener(this);
        mSelectImageView.setOnClickListener(this);
        mDeleteImageView.setOnClickListener(this);
    }

    public void setOnSelectChange(View.OnClickListener onSelelctChange) {
        mOnClickListener = onSelelctChange;
    }

    private void initView() {
        initRefreshLayout();
        initRecyclerView();
        getPresenter().initView(this);
        mBackImageView = findViewById(R.id.back_imageview);
        mSelectImageView = findViewById(R.id.select_all_imageview);
        mDeleteImageView = findViewById(R.id.delete_imageview);

        mGridLayoutManager = new GridLayoutManager(this, 3);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position < mPhotoAlbumAdapter.getItemCount()) {
                    if (mPhotoAlbumAdapter.getMultiMediaItem(position) != null
                            && mPhotoAlbumAdapter.getMultiMediaItem(position).path == null) {
                        return 3;
                    } else {
                        return 1;
                    }
                }
                return 3;
            }
        });
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mPhotoAlbumAdapter = new PhotoAlbumAdapter(this, new ArrayList<MultiMediaItemBean>(), mGridLayoutManager, 3);
        if (hadSelectNum != -1) {
            mPhotoAlbumAdapter.change2SelectType(true);
        }
        mRecyclerView.setAdapter(mPhotoAlbumAdapter);
        mPhotoAlbumAdapter.setOnClickListener(this);
        mPhotoAlbumAdapter.setOnSelectChangeLis(mOnClickListener);
    }

    @Override
    public void loadData() {
        if (getPresenter() != null) {
            getPresenter().loadData();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_imageview:
                finish();
                break;
            case R.id.select_all_imageview:
                if (isCheckAll) {
                    isCheckAll = false;
                    mSelectImageView.setBackgroundResource(R.mipmap.icon_check_off);
                } else {
                    isCheckAll = true;
                    mSelectImageView.setBackgroundResource(R.mipmap.icon_check_on);
                }
                mPhotoAlbumAdapter.checkAll(isCheckAll);
                break;
            case R.id.delete_imageview:
                mDialogUtils.createDialogWith2Btn("确认删除", "删除以后将无法恢复，是否确认删除？", "取消", "确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialogUtils.cancelDialog();
                        switch (view.getId()){
                            case R.id.dialog_btn_sure:
                                deletePhoto();
                                break;
                        }
                    }
                });
                break;

            default:
                break;
        }
    }

    /**
     * 删除照片
     */
    private void deletePhoto() {
        getPresenter().deleteFile();
    }

    @Override
    public void setData(List<MultiMediaItemBean> beanList) {
        finishRefresh();
        mPhotoAlbumAdapter.setMultiMediaItemBeans(beanList);
    }
}
