package com.gdu.command.ui.secondmap;

import com.amap.api.services.help.Tip;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gdu.command.R;

import java.util.List;

/**
 * 地址搜索的适配器.
 * @author wixche
 */
public class InputTipsAdapter extends BaseQuickAdapter<Tip, BaseViewHolder> {

    public InputTipsAdapter(List<Tip> data) {
        super(R.layout.item_map_search, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Tip bean) {
        holder.setText(R.id.item_map_search_content, bean.getName());
    }

}
