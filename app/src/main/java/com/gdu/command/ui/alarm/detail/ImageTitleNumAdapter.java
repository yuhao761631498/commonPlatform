package com.gdu.command.ui.alarm.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.command.R;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

/**
 * 自定义布局，图片+标题+数字指示器
 */
public class ImageTitleNumAdapter extends BannerAdapter<String, ImageTitleNumAdapter.BannerViewHolder> {
    private Context mContext;

    public ImageTitleNumAdapter(List<String> mDatas) {
        //设置数据，也可以调用banner提供的方法
        super(mDatas);
    }

    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        //注意布局文件，item布局文件要设置为match_parent，这个是viewpager2强制要求的
        //或者调用BannerUtils.getView(parent,R.layout.banner_image_title_num);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_banner_image_title_num, parent,
                false);
        return new BannerViewHolder(view);
    }

    //绑定数据
    @Override
    public void onBindView(BannerViewHolder holder, String data, int position, int size) {
        final String realPicPath = CommonUtils.getSinglePicRealPath(data);
        MyImageLoadUtils.loadImage(mContext, realPicPath, R.mipmap.icon_case_default1, holder.imageView);
        //可以在布局文件中自己实现指示器，亦可以使用banner提供的方法自定义指示器，目前样式较少，后面补充
        holder.numIndicator.setText((position + 1) + "/" + size);
    }


    class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        TextView numIndicator;

        public BannerViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.iv_content);
            title = view.findViewById(R.id.tv_bannerTitle);
            numIndicator = view.findViewById(R.id.tv_numIndicator);
        }
    }

}
