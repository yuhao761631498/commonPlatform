package com.gdu.command.ui.duty;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gdu.command.ui.duty.group.GroupRecyclerAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 适配器
 * Created by huanghaibin on 2017/12/4.
 */
public class ArticleAdapter extends GroupRecyclerAdapter<String, DutyInfoBean> {

    private Context context;

    public ArticleAdapter(Context context) {
        super(context);
        this.context = context;
    }

    public void setDutyData(List<DutyForDayBean.DutyMemberBean> beans) {
        LinkedHashMap<String, List<DutyInfoBean>> map = new LinkedHashMap<>();
        List<String> titles = new ArrayList<>();
        if (beans != null) {
            for (DutyForDayBean.DutyMemberBean dutyMemberBean : beans) {
                String key = "|【" + dutyMemberBean.getDutyTypeName() + "】";
                DutyInfoBean dutyInfoBean = create(dutyMemberBean.getUserName(), 0,
                        "白班".equals(dutyMemberBean.getShiftdName()) ? 0 : 1, dutyMemberBean.getUserPhone(),
                        dutyMemberBean.getDutyTypeName());
                if (map.containsKey(key)) {
                    List<DutyInfoBean> dutyInfoBeans = map.get(key);
                    if (dutyInfoBeans != null) {
                        dutyInfoBeans.add(dutyInfoBean);
                    } else {
                        List<DutyInfoBean> list = new ArrayList<>();
                        list.add(dutyInfoBean);
                        map.put(key, list);
                    }
                } else {
                    List<DutyInfoBean> list = new ArrayList<>();
                    list.add(dutyInfoBean);
                    map.put(key, list);
                    titles.add(key);
                }
            }
            //        map.put("|【带班领导】", create(0));
//        map.put("|【值班员】", create(1));
//        map.put("|【带班经理】", create(2));
//        titles.add("|【带班领导】");
//        titles.add("|【值班员】");
//        titles.add("|【带班经理】");
        }
        resetGroups(map, titles);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new ArticleViewHolder(mInflater.inflate(com.gdu.command.R.layout.item_duty_people, parent, false));
    }

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder holder, final DutyInfoBean item, final int position) {
        ArticleViewHolder h = (ArticleViewHolder) holder;
        h.tv_duty_name.setText(item.getName());
        h.iv_sex.setImageResource(item.getSex() == 0 ? com.gdu.command.R.mipmap.icon_meal :
                com.gdu.command.R.mipmap.icon_female);
        h.tv_work_type.setText(item.getWorkType() == 0 ? "白班（8:30-17:30）" : "晚班（17:30-8:30）");
        h.iv_work_type.setImageResource(item.getWorkType() == 0 ? com.gdu.command.R.mipmap.icon_day_work : com.gdu.command.R.mipmap.icon_night_work);

        h.tv_duty_phone_number.setText(item.getPhoneNumber());

        if (TextUtils.isEmpty(item.getPhoneNumber())) {
            h.tv_call_duty.setVisibility(View.INVISIBLE);
        } else {
            h.tv_call_duty.setVisibility(View.GONE);
        }

        h.tv_call_duty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position, item.getPhoneNumber());
                }
            }
        });//设置监听器
    }

    private static class ArticleViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_duty_name,
                tv_work_type, tv_duty_phone_number, tv_call_duty;

        private ImageView iv_sex, iv_work_type;

        private ArticleViewHolder(View itemView) {
            super(itemView);
            tv_duty_name = itemView.findViewById(com.gdu.command.R.id.tv_duty_name);
            tv_work_type = itemView.findViewById(com.gdu.command.R.id.tv_work_type);
            tv_duty_phone_number = itemView.findViewById(com.gdu.command.R.id.tv_duty_phone_number);
            tv_call_duty = itemView.findViewById(com.gdu.command.R.id.tv_call_duty);
            iv_sex = itemView.findViewById(com.gdu.command.R.id.iv_sex);
            iv_work_type = itemView.findViewById(com.gdu.command.R.id.iv_work_type);
        }
    }

    private static DutyInfoBean create(String name, int sex, int workType, String phoneNumber, String jobTitle) {
        DutyInfoBean dutyInfoBean = new DutyInfoBean();
        dutyInfoBean.setName(name);
        dutyInfoBean.setSex(sex);
        dutyInfoBean.setWorkType(workType);
        dutyInfoBean.setPhoneNumber(phoneNumber);
        dutyInfoBean.setJobTitle(jobTitle);
        return dutyInfoBean;
    }

    private static List<DutyInfoBean> create(int p) {
        List<DutyInfoBean> list = new ArrayList<>();
        if (p == 0) {
            list.add(create("张三", 0, 0, "188652412", "带班领导"));
            list.add(create("小红", 1, 1, "34635653", "带班领导"));
            list.add(create("小李", 0, 0, "154626", "带班领导"));
            list.add(create("王麻子", 1, 0, "1546265", "带班领导"));
        } else if (p == 1) {
            list.add(create("张丽丽", 0, 0, "188652412", "值班员"));
            list.add(create("王菲菲", 1, 1, "34635653", "值班员"));
            list.add(create("成龙", 0, 0, "154626", "值班员"));
            list.add(create("李连杰", 1, 0, "1546265", "值班员"));
        } else if (p == 2) {
            list.add(create("张三丰", 0, 0, "188652412", "带班经理"));
            list.add(create("张无忌", 1, 1, "34635653", "带班经理"));
            list.add(create("赵敏", 0, 0, "154626", "带班经理"));
            list.add(create("周芷若", 1, 0, "1546265", "带班经理"));
        }
        return list;
    }


    public interface OnItemClickListener {
        //参数(父组件，当前单击的View,单击的View的位置，数据)
        void onItemClick(int position, String data);
    }


    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
