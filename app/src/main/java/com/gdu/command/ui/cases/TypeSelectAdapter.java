package com.gdu.command.ui.cases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdu.command.R;
import com.wx.wheelview.adapter.BaseWheelAdapter;

/**
 *
 */
public class TypeSelectAdapter extends BaseWheelAdapter<TypeCodeBean.DataBean> {
	private Context mContext;

	public TypeSelectAdapter(Context context) {
		mContext = context;
	}

	@Override
	protected View bindView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_type_dialog_list, null);
			viewHolder.textView = convertView.findViewById(R.id.item_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.textView.setText(mList.get(position).getTypeName());
		return convertView;
	}

	class ViewHolder {
		TextView textView;
	}
}