package com.gdu.picktime.listener;


import com.gdu.picktime.bean.DateBean;
import com.gdu.picktime.bean.DateType;

/**
 * Created by codbking on 2016/9/22.
 */

public interface WheelPickerListener {
     void onSelect(DateType type, DateBean bean);
}
