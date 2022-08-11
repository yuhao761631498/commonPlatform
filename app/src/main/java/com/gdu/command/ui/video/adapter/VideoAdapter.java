package com.gdu.command.ui.video.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.command.R;
import com.gdu.command.ui.video.model.ChildrenBean;
import java.util.List;

public class VideoAdapter extends BaseAdapter {

    private final Context context;
    private List<ChildrenBean> childrenBeans;

    public VideoAdapter(Context context, List<ChildrenBean> data) {
        this.context = context;
        childrenBeans = data;
    }

    @Override
    public int getCount() {
        if (childrenBeans == null) {
            return 0;
        }
        return childrenBeans.size();
    }

    @Override
    public Object getItem(int i) {
        return childrenBeans.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_video, viewGroup, false);
            viewHolder.tv_video_name = view.findViewById(R.id.tv_video_name);
            viewHolder.iv_video_cover = view.findViewById(R.id.iv_video_cover);
            viewHolder.iv_video_more = view.findViewById(R.id.iv_video_more);
            view.setTag(viewHolder);
        } else {
            viewHolder = ((ViewHolder) view.getTag());
        }
        ChildrenBean data = childrenBeans.get(i);
        final String labelStr = data.getLabel();
        if (data.getOnlineStatus().equals("1")) {
            if ("公安陈家台".equals(labelStr)) {
                MyImageLoadUtils.loadRoundedImageWithDrawable(context, R.drawable.ic_video_chenjiatai,5,
                        R.mipmap.icon_device_online, viewHolder.iv_video_cover);
            } else if ("新垸村".equals(labelStr)) {
                MyImageLoadUtils.loadRoundedImageWithDrawable(context, R.drawable.ic_video_xinhuangcun,5,
                        R.mipmap.icon_device_online, viewHolder.iv_video_cover);
            } else if ("弥市镇陈家湾村委会".equals(labelStr)) {
                MyImageLoadUtils.loadRoundedImageWithDrawable(context, R.drawable.ic_video_chenjiawan,5,
                        R.mipmap.icon_device_online, viewHolder.iv_video_cover);
            } else if ("公安沿江".equals(labelStr)) {
                MyImageLoadUtils.loadRoundedImageWithDrawable(context, R.drawable.ic_video_gonganyanjiang,5,
                        R.mipmap.icon_device_online, viewHolder.iv_video_cover);
            } else if ("江陵文村".equals(labelStr)) {
                MyImageLoadUtils.loadRoundedImageWithDrawable(context, R.drawable.ic_video_jianglingwencun,5,
                        R.mipmap.icon_device_online, viewHolder.iv_video_cover);
            } else if ("公安埠河荆南六组".equals(labelStr)) {
                MyImageLoadUtils.loadRoundedImageWithDrawable(context, R.drawable.ic_video_jingnanliuzu,5,
                        R.mipmap.icon_device_online, viewHolder.iv_video_cover);
            } else if ("龙州".equals(labelStr)) {
                MyImageLoadUtils.loadRoundedImageWithDrawable(context, R.drawable.ic_video_longzhou,5,
                        R.mipmap.icon_device_online, viewHolder.iv_video_cover);
            } else if ("荆州太平口".equals(labelStr)) {
                MyImageLoadUtils.loadRoundedImageWithDrawable(context, R.drawable.ic_video_jingzhoutaipingkou,5,
                        R.mipmap.icon_device_online, viewHolder.iv_video_cover);
            } else if ("荆州金城超市".equals(labelStr)) {
                MyImageLoadUtils.loadRoundedImageWithDrawable(context, R.drawable.ic_video_jinglingchaoshi,5,
                        R.mipmap.icon_device_online, viewHolder.iv_video_cover);
            } else if ("公安北闸风景区".equals(labelStr)) {
                MyImageLoadUtils.loadRoundedImageWithDrawable(context, R.drawable.ic_video_gonganbeizha,5,
                        R.mipmap.icon_device_online, viewHolder.iv_video_cover);
            } else if ("簇63街道站7-1".equals(labelStr)){
                MyImageLoadUtils.loadRoundedImageWithDrawable(context, R.drawable.ic_video_63jiedao71,5,
                        R.mipmap.icon_device_online, viewHolder.iv_video_cover);
            }else if ("公安滨江花园".equals(labelStr)){
                MyImageLoadUtils.loadRoundedImageWithDrawable(context, R.drawable.ic_video_bingjianghuayuan,5,
                        R.mipmap.icon_device_online, viewHolder.iv_video_cover);
            }else if ("公安收费站".equals(labelStr)){
                MyImageLoadUtils.loadRoundedImageWithDrawable(context, R.drawable.ic_video_shoufeizhan,5,
                        R.mipmap.icon_device_online, viewHolder.iv_video_cover);
            }else if ("公安五洲".equals(labelStr)){
                MyImageLoadUtils.loadRoundedImageWithDrawable(context, R.drawable.ic_video_wuzhou,5,
                        R.mipmap.icon_device_online, viewHolder.iv_video_cover);
            }else if ("公安杨厂红胜村".equals(labelStr)){
                MyImageLoadUtils.loadRoundedImageWithDrawable(context, R.drawable.ic_video_yangchanghongshengcun,5,
                        R.mipmap.icon_device_online, viewHolder.iv_video_cover);
            }else if ("公安杨厂上码头".equals(labelStr)){
                MyImageLoadUtils.loadRoundedImageWithDrawable(context, R.drawable.ic_video_yangchangshangmatou,5,
                        R.mipmap.icon_device_online, viewHolder.iv_video_cover);
            }else if ("公安杨场二砖瓦厂".equals(labelStr)){
                MyImageLoadUtils.loadRoundedImageWithDrawable(context, R.drawable.ic_video_yangchangerzhuanwachang,5,
                        R.mipmap.icon_device_online, viewHolder.iv_video_cover);
            }else if ("纪南管委会".equals(labelStr)){
                MyImageLoadUtils.loadRoundedImageWithDrawable(context, R.drawable.ic_video_jinanguanweihui,5,
                        R.mipmap.icon_device_online, viewHolder.iv_video_cover);
            }else if ("江陵县荆松水泥".equals(labelStr)){
                MyImageLoadUtils.loadRoundedImageWithDrawable(context, R.drawable.ic_video_jingsongshuini,5,
                        R.mipmap.icon_device_online, viewHolder.iv_video_cover);
            }else if ("江陵县沿江路中".equals(labelStr)){
                MyImageLoadUtils.loadRoundedImageWithDrawable(context, R.drawable.ic_video_jinglingxianyanjiangzhonglu,5,
                        R.mipmap.icon_device_online, viewHolder.iv_video_cover);
            }else if ("荆州区园博园".equals(labelStr)){
                MyImageLoadUtils.loadRoundedImageWithDrawable(context, R.drawable.ic_video_yuanboyuan,5,
                        R.mipmap.icon_device_online, viewHolder.iv_video_cover);
            }else if ("开发区旺港码头".equals(labelStr)){
                MyImageLoadUtils.loadRoundedImageWithDrawable(context, R.drawable.ic_video_wanggangmatou,5,
                        R.mipmap.icon_device_online, viewHolder.iv_video_cover);
            }else if ("天主堂".equals(labelStr)){
                MyImageLoadUtils.loadRoundedImageWithDrawable(context, R.drawable.ic_video_tiantangzhu,5,
                        R.mipmap.icon_device_online, viewHolder.iv_video_cover);
            } else {
                viewHolder.iv_video_cover.setImageResource(R.mipmap.icon_device_online);
            }
        } else {
//            mDeviceStatusTextView.setText("离线");
//            mDeviceStatusTextView.setBackgroundColor(getActivity().getResources().getColor(R.color.color_F24343));
            viewHolder.iv_video_cover.setImageResource(R.mipmap.icon_device_offline);
        }
        viewHolder.tv_video_name.setText(labelStr == null ? "" : labelStr);

        viewHolder.iv_video_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoMoreClickListener != null) {
                    videoMoreClickListener.moreClick(i);
                }
            }
        });
        return view;
    }

    public void setList(List<ChildrenBean> data) {
        childrenBeans = data;
        notifyDataSetChanged();
    }

    class ViewHolder {
        ImageView iv_video_cover;
        TextView tv_video_name;
        ImageView iv_video_more;
    }

    private VideoMoreClickListener videoMoreClickListener;

    public interface VideoMoreClickListener {
        void moreClick(int position);
    }

    public void setVideoMoreClickListener(VideoMoreClickListener videoMoreClickListener) {
        this.videoMoreClickListener = videoMoreClickListener;
    }
}
