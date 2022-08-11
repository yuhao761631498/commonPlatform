package com.gdu.command.ui.video.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.command.R;
import com.gdu.model.base.MultiMediaItemBean;
import com.gdu.utils.CPStringUtils;
import com.gdu.utils.ViewUtils;

import java.util.Calendar;
import java.util.List;

public class PhotoAlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<MultiMediaItemBean> mMultiMediaItemBeans;
    private GridLayoutManager mGridLayoutManager;
    private View.OnClickListener mHeaderCheckBoxOnClickListener;
    private View.OnClickListener mGridItemClickListener;
    private View.OnClickListener mOnClickListener;
    private View.OnClickListener mOnSelectChangeLis;

    public ViewHolderFooter viewHolderFooter;

    public ViewHolderHead holderHead;


    /**
     * <p>当前是否是selectType</p>
     */
    private boolean isSelectType;

    private int mNumOfRow = 4;

    public PhotoAlbumAdapter(Context context, List<MultiMediaItemBean> data,
                         GridLayoutManager gridLayoutManager, int numOfRow) {
        mNumOfRow = numOfRow;
        mContext = context;
        mMultiMediaItemBeans = data;
        mGridLayoutManager = gridLayoutManager;
        initListener();
    }


    public void setMultiMediaItemBeans(List<MultiMediaItemBean> data){
        mMultiMediaItemBeans = data;
        notifyDataSetChanged();
    }

    /**
     * 选择所有的图片和视频
     */
    public void checkAll(boolean isChecked){
        for (int i = 0; i < mMultiMediaItemBeans.size(); i++) {
            MultiMediaItemBean subItem = mMultiMediaItemBeans.get(i);
            subItem.isSelect = isChecked;
        }
        notifyDataSetChanged();
    }

    private void initListener() {
        mHeaderCheckBoxOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                int position = (Integer) v.getTag();
                List<MultiMediaItemBean> items = mMultiMediaItemBeans;

                MultiMediaItemBean headItem = items.get(position);
                headItem.isSelect = cb.isChecked();
                View view = new View(mContext);
                // 将本月所有图片全部设置为选中
                int lastItem = -1;  // 记录本月最后一个图片的位置
                for (int i = position + 1; i < items.size(); i++) {
                    MultiMediaItemBean subItem = items.get(i);
                    if (subItem.lastModified >= headItem.lastModified) {
                        subItem.isSelect = cb.isChecked();
                        lastItem = i;
                        if (subItem != null && mOnSelectChangeLis != null) {
                            view.setTag(subItem);
                            mOnSelectChangeLis.onClick(view);
                        }
                    } else {
                        break;
                    }
                }

                if (lastItem != -1) {
                    // 全选时刷新当月所有图片（全选按钮自己 - header）
                    notifyItemRangeChanged(position, lastItem - position + 1);
                }
            }
        };

        mGridItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSelectType) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onClick(v);
                    }
                } else {
                    MultiMediaItemBean bean = (MultiMediaItemBean) v.getTag();
                    int position = bean.position;

                    List<MultiMediaItemBean> items = mMultiMediaItemBeans;
                    MultiMediaItemBean item = items.get(position);
                    item.isSelect = !mMultiMediaItemBeans.get(position).isSelect;

                    // 获取当前月份所在的位置
                    int headerIdx = getHeaderIndex(position);

                    if (headerIdx != -1) {
                        // 判断当前月份是否所有的item全选，再来更新header的选择状态
                        items.get(headerIdx).isSelect = isSectionAllChecked(position);
                        notifyItemChanged(headerIdx);   // 刷新当前点击的item
                    }
                    notifyItemChanged(position);    // 刷新header（全选按钮）
                    if (mOnSelectChangeLis != null) {
                        mOnSelectChangeLis.onClick(v);
                    }
                }
            }
        };
    }

    /**
     * <p>切换选择模式 和 非选择模式</p>
     *
     * @param isSelectType
     */
    public void change2SelectType(boolean isSelectType) {
        this.isSelectType = isSelectType;
        for (int i = 0; i < mMultiMediaItemBeans.size(); i++) {
            mMultiMediaItemBeans.get(i).isSelect = false;
        }
        notifyDataSetChanged();
    }


    private int getHeaderIndex(int position) {
        while (position > 0) {
            position--;
            if (getItemViewType(position) == ITEM_TYPE.ITEM_HEAD.ordinal()) {
                return position;
            }
        }
        return -1;
    }


    /**
     * 点击某张图片的时候，判断图片所在的月份包含的图片是否都全部选中了
     *
     * @param position - 点击的图片位置
     * @return true - 当前月份所有的图片都选中了; false - 至少有一张图片未选中
     */
    private boolean isSectionAllChecked(int position) {
        final int HEAD = ITEM_TYPE.ITEM_HEAD.ordinal();

        // 判断同一个月里面，当前位置之前所有的图片是否全选
        for (int i = position - 1; i >= 0; i--) {
            if (getItemViewType(i) == HEAD) {
                break;
            } else {
                if (mMultiMediaItemBeans.get(i).isSelect == false) {
                    return false;
                }
            }
        }

        // 判断同一个月里面，当前位置（包括）之后所有的图片是否全选
        for (int i = position; i < mMultiMediaItemBeans.size(); i++) {
            if (getItemViewType(i) == HEAD) {
                break;
            } else {
                if (mMultiMediaItemBeans.get(i).isSelect == false) {
                    return false;
                }
            }
        }
        return true;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == ITEM_TYPE.ITEM_HEAD.ordinal()) {
            if (mNumOfRow == 3) {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_multimedia_head_land, null);
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_multimedia_head, null);
            }
            holderHead = new ViewHolderHead(view);
            return holderHead;
        } else if (viewType == ITEM_TYPE.ITEM_CONTENT.ordinal()) {
            if (mNumOfRow == 3) {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_land_multimedia_content, null);
            } else {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_multimedia_content, null);
            }
            View content1 = view.findViewById(R.id.fl_item_multiMedia_content);
            ViewUtils.setViewHeight(content1, ViewUtils.getWindowWidth(mContext) / mNumOfRow);
            return new ViewHolderContent(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.footer_view, parent, false);
            viewHolderFooter = new ViewHolderFooter(view);
            return viewHolderFooter;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderHead) {
            ((ViewHolderHead) holder).checkBox.setTag(position);
            //((ViewHolderHead) holder).checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
            ((ViewHolderHead) holder).checkBox.setOnClickListener(mHeaderCheckBoxOnClickListener);
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.setTimeInMillis(mMultiMediaItemBeans.get(position).lastModified);
            String data_txt = mContext.getResources().getString(R.string.Date_Format,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            ((ViewHolderHead) holder).textView.setText(data_txt);
            if (!isSelectType) {
                ((ViewHolderHead) holder).checkBox.setVisibility(View.INVISIBLE);
            } else {
                ((ViewHolderHead) holder).checkBox.setVisibility(View.VISIBLE);
            }

            if (mMultiMediaItemBeans.get(position).isSelect) {
                ((ViewHolderHead) holder).checkBox.setChecked(true);
            } else {
                ((ViewHolderHead) holder).checkBox.setChecked(false);
            }

        } else if (holder instanceof ViewHolderContent) {
            final ImageView img = ((ViewHolderContent) holder).imageView;
            if (mMultiMediaItemBeans.get(position).type == MultiMediaItemBean.IMAGE) {    // 在照片中 。。。。。。
                String path = "file://" + mMultiMediaItemBeans.get(position).coverPath;
                TagBean tagBean = (TagBean) img.getTag();
                if (tagBean == null || !tagBean.path.toString().equals(path)) {
//                    loadImg(img, path, position);
                    MyImageLoadUtils.loadImage(mContext, path, img);
                }
                ((ViewHolderContent) holder).imageView.setTag(new TagBean(position, path));
                ((ViewHolderContent) holder).iconVideo.setVisibility(View.GONE);
                ((ViewHolderContent) holder).tvDuration.setVisibility(View.GONE);

                if (((ViewHolderContent) holder).tv_size != null) {
                    ((ViewHolderContent) holder).tv_size.setText(mMultiMediaItemBeans.get(position).size);
                }
            } else {                                                       //在视频中。。。。。。。。
                // 根据是否是云媒体判断，获取不同视频的CoverPic
//                String path =  "file://" + CPStringUtils.createVideoThumbnailName(mMultiMediaItemBeans.get(position).name);
                String path = "file://" + mMultiMediaItemBeans.get(position).coverPath;
                TagBean tagBean = (TagBean) img.getTag();
                if (tagBean == null || !tagBean.path.toString().equals(path)) {
//                    loadImg(img, path, position);
                    MyImageLoadUtils.loadImage(mContext, path, img);
                }
                img.setTag(new TagBean(position, path));
                ((ViewHolderContent) holder).imageView.setTag(new TagBean(position, path));
                ((ViewHolderContent) holder).tvDuration.setText(CPStringUtils.duration2Str(mMultiMediaItemBeans.get(position).duration / 1000));// 媒体事件设置
                ((ViewHolderContent) holder).tvDuration.setVisibility(View.VISIBLE);// 云媒体时间部准确，暂时隐藏 2017/4/19
                ((ViewHolderContent) holder).iconVideo.setVisibility(View.VISIBLE);

                if (((ViewHolderContent) holder).tv_size != null) {
                    ((ViewHolderContent) holder).tv_size.setText(mMultiMediaItemBeans.get(position).size);
                }
            }

            if (!mMultiMediaItemBeans.get(position).isSelect) {
                ((ViewHolderContent) holder).view.setVisibility(View.GONE);
                ((ViewHolderContent) holder).check.setVisibility(View.GONE);
            } else {
                ((ViewHolderContent) holder).view.setVisibility(View.VISIBLE);
                ((ViewHolderContent) holder).check.setVisibility(View.VISIBLE);
            }


            if ((mMultiMediaItemBeans.get(position).HadDownSource)||mMultiMediaItemBeans.get(position).showHadDowned){
                ((ViewHolderContent) holder).iconDowned.setVisibility(View.GONE);
            }else{
                ((ViewHolderContent) holder).iconDowned.setVisibility(View.GONE);
            }


            if (mGridItemClickListener != null) {
                mMultiMediaItemBeans.get(position).position = position;
                holder.itemView.setTag(mMultiMediaItemBeans.get(position));
                holder.itemView.setOnClickListener(mGridItemClickListener);
            }
        } else if (holder instanceof ViewHolderFooter) {
//            if (isCloudMedia) {
//                int itemCount = getItemCount();
//                if (itemCount - 1 >= 28) {
//                    ((ViewHolderFooter) holder).tv_footer.setVisibility(View.VISIBLE);
//                }
//            }
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setOnSelectChangeLis(View.OnClickListener onSelectChangeLis) {
       mOnClickListener = onSelectChangeLis;
    }

    public class ViewHolderHead extends RecyclerView.ViewHolder {
        TextView textView;
        CheckBox checkBox;

        public ViewHolderHead(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_multiMedia_date);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox_multiMedia_date);
        }
    }

    public class ViewHolderContent extends RecyclerView.ViewHolder {
        TextView tv_size;
        ImageView imageView;
        View view;
        View check;
        ImageView iconVideo;
        TextView tvDuration;
        ImageView iconDowned;

        public ViewHolderContent(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_item_multiMedia_content);
            view = itemView.findViewById(R.id.v_item_multiMedia_content);
            check = itemView.findViewById(R.id.iv_item_multiMedia_content_check);
            iconVideo = (ImageView) itemView.findViewById(R.id.iv_icon_video);
            tvDuration = (TextView) itemView.findViewById(R.id.tv_video_duration);
            iconDowned = (ImageView) itemView.findViewById(R.id.iv_item_multiMedia_HadDown);
            tv_size = ((TextView) itemView.findViewById(R.id.tv_firmware_progress));
        }
    }

    public class ViewHolderFooter extends RecyclerView.ViewHolder {
        private final TextView tv_footer;
        private final TextView tv_footer_all;

        public ViewHolderFooter(View itemView) {
            super(itemView);
            tv_footer = ((TextView) itemView.findViewById(R.id.tv_footer));
            tv_footer_all = ((TextView) itemView.findViewById(R.id.tv_footer_all));
        }

        public void changeState(boolean isLoading) {
            if (isLoading) {
                tv_footer.setVisibility(View.VISIBLE);
            } else {
                tv_footer.setVisibility(View.GONE);
            }
        }

        public void showAll() {
            tv_footer.setVisibility(View.VISIBLE);
            tv_footer.setText(mContext.getString(R.string.load_all));
        }
    }

    @Override
    public int getItemCount() {
        if (mMultiMediaItemBeans != null) {
            return mMultiMediaItemBeans.size();
        }
        return 0;
    }

    public MultiMediaItemBean getMultiMediaItem(int position){
        if (mMultiMediaItemBeans != null && (position < mMultiMediaItemBeans.size())) {
          return mMultiMediaItemBeans.get(position);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mMultiMediaItemBeans.size()) {
            return ITEM_TYPE.ITEM_FOOT.ordinal();   //到最底部上拉加载更多  余浩
        } else {
            if (mMultiMediaItemBeans.get(position).path == null) {
                return ITEM_TYPE.ITEM_HEAD.ordinal();
            } else {
                return ITEM_TYPE.ITEM_CONTENT.ordinal();
            }
        }
    }

    public enum ITEM_TYPE {
        ITEM_HEAD, ITEM_CONTENT, ITEM_FOOT
    }

    public static class TagBean {
        public TagBean(int position, String path) {
            this.path = path;
            this.position = position;
        }

        public int position;

        public String path;
    }
}
