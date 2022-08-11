package com.gdu.command.ui.video.presenter;

import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.command.ui.video.model.PhotoAlbumBiz;
import com.gdu.command.ui.video.view.IPhotoAlbumView;
import com.gdu.model.base.MultiMediaItemBean;

import java.io.File;
import java.util.List;

public class PhotoAlbumPresenter extends BasePresenter {

    private PhotoAlbumBiz mPhotoAlbumBiz;
    private IPhotoAlbumView mPhotoAlbumView;
    private List<MultiMediaItemBean> mMultiMediaList;


    public PhotoAlbumPresenter(){
        mPhotoAlbumBiz = new PhotoAlbumBiz();
    }

    public void initView(IPhotoAlbumView photoAlbumView){
        mPhotoAlbumView = photoAlbumView;
    }

    public void loadData(){
       List<MultiMediaItemBean> beanList = mPhotoAlbumBiz.getImageListAndSort();
        mMultiMediaList = beanList;
        mPhotoAlbumView.setData(beanList);
    }

    /**
     * 删除选中的图片或视频
     */
    public void deleteFile(){
        for (int i = 0; i < mMultiMediaList.size(); i++) {
            MultiMediaItemBean bean = mMultiMediaList.get(i);
            if (bean.path != null && bean.isSelect) {
                mMultiMediaList.remove(i);
                File file = new File(bean.path);
                if (file.isFile()) {
                    file.delete();
                }
                i--;
            }
        }

        for (int i = 0; i < mMultiMediaList.size(); i++) {
            MultiMediaItemBean bean = mMultiMediaList.get(i);
            if (bean.path == null) {
                if (i == mMultiMediaList.size() - 1) {
                    mMultiMediaList.remove(i);
                } else if (i + 1 < mMultiMediaList.size() - 1) {
                    if (mMultiMediaList.get(i + 1).lastModified < bean.lastModified) {
                        mMultiMediaList.remove(i);
                        i--;
                    }
                }
            }
        }
        mPhotoAlbumView.setData(mMultiMediaList);
    }

}
