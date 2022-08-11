package com.gdu.baselibrary.base;

import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

/**
 * RecyclerView Adapter的基类
 */

public abstract class BaseRVAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    public BaseRVAdapter(int layoutResId) {
        super(layoutResId);
    }


    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
        onBindVH(helper, item, getItemPosition(item) - getHeaderLayoutCount());
    }

    public abstract void onBindVH(final BaseViewHolder holder, T data, int position);


    protected void setCheckedChange(CompoundButton cb, final int position) {
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onViewCheckedChangeListener != null) {
                    onViewCheckedChangeListener.onViewCheckedChanged(buttonView, isChecked, position);
                }
            }
        });
    }

    /**
     * @param onViewClickListener
     */
    public OnViewCheckedChangeListener onViewCheckedChangeListener;

    public void setOnViewCheckedChangeListener(OnViewCheckedChangeListener onViewCheckedChangeListener) {
        this.onViewCheckedChangeListener = onViewCheckedChangeListener;
    }

    public interface OnViewCheckedChangeListener {
        void onViewCheckedChanged(CompoundButton buttonView, boolean isChecked, int position);
    }

}
