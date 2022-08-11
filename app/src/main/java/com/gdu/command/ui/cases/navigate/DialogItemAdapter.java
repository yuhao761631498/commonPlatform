package com.gdu.command.ui.cases.navigate;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gdu.command.R;

import java.util.List;

/**
 * 我的界面-中间RecyclerView适配器.
 * @author wixche
 */
public class DialogItemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public DialogItemAdapter(List<String> data) {
        super(R.layout.item_takepicture_album_dialog, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String bean) {
        holder.setText(R.id.tv_bottom_dialog_item, bean);
    }

}
