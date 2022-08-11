package com.gdu.command.ui.base.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;

import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.command.R;
import com.gdu.command.ui.setting.Dept;
import com.gdu.command.ui.setting.Node;
import com.gdu.command.ui.setting.NodeHelper;
import com.gdu.command.ui.setting.NodeTreeAdapter;
import com.gdu.command.ui.setting.SelectDepartmentListEntity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.gdu.baselibrary.utils.ScreenUtil.getScreenHeight;

/**
 * 选择部门树形菜单
 */
public class SelectDepartmentBottomDialog extends BottomSheetDialog {

    private Context context;
    private ListView mListView;
    private NodeTreeAdapter mAdapter;
    private LinkedList<Node> mLinkedList = new LinkedList<>();
    private SelectDepartmentListEntity entity;
    private List<SelectDepartmentListEntity.DataDTO> nodeList;
    private OnBottomSelectListener onBottomSelectListener;
    private List<Node> data = new ArrayList<>();

    public SelectDepartmentBottomDialog(Context context, SelectDepartmentListEntity bean) {
        super(context, R.style.DialogTheme);
        this.context = context;
        this.entity = bean;
    }

    public void showSelectDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.select_dept_layout, null);
        setContentView(view);

        mListView = view.findViewById(R.id.id_tree);
        mAdapter = new NodeTreeAdapter(context, mListView, mLinkedList);
        mListView.setAdapter(mAdapter);
        setViewScrollEnable();
        initData();

        Window window = getWindow();
        if (window != null) {
            setDialogHeight(context, view);
        }
        setCanceledOnTouchOutside(true);
    }

    private void initData(){
        nodeList = entity.getData();
        bindData(0, nodeList);
        mLinkedList.addAll(NodeHelper.sortNodes(data));
        mAdapter.notifyDataSetChanged();

        mAdapter.setOnTreeMenuItemClickListener(new NodeTreeAdapter.OnTreeMenuItemClickListener() {
            @Override
            public void onClick(String value, int id, String deptCode) {
                if (onBottomSelectListener != null) {
                    onBottomSelectListener.onClick(value, deptCode);
                }
                dismiss();
            }
        });
    }

    /**
     * 遍历数据源取出数据
     * 通过id确定节点之间的对应关系，生成层级结构
     * @param parentId
     * @param list
     */
    private void bindData(int parentId, List<SelectDepartmentListEntity.DataDTO> list) {
        if (list != null && !list.isEmpty()) {
            for (SelectDepartmentListEntity.DataDTO node : list) {
                data.add(new Dept(node.getId(), parentId, node.getDeptName(), node.getDeptCode()));
                bindData(node.getId(), node.getChildren());
            }
        }
    }

    public void setOnItemSelectedListener(OnBottomSelectListener listener) {
        this.onBottomSelectListener = listener;
    }

    public interface OnBottomSelectListener {
        void onClick(String value, String id);
    }

    /**
     * 解决滑动冲突问题
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setViewScrollEnable() {
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!mListView.canScrollVertically(-1)) {
                    mListView.requestDisallowInterceptTouchEvent(false);
                } else {
                    mListView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
    }

    /**
     * 根据手机的分辨率将 dp 转成为 px(像素),返回int类型值
     * @param context
     * @param dpValue
     */
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        int pxValue = (int) (dpValue * scale + 0.5f);
        return pxValue;
    }

    /**
     * 设置dialog高度为屏幕高度减去状态栏的高度
     * 设置默认弹起的高度为屏幕高度减去状态栏的高度
     * @param context
     * @param view
     */
    public void setDialogHeight(Context context, View view) {
        //获取屏幕高度
        int screenHeight = getScreenHeight(context);
        //导航标题栏的高度
        int titleBarHeight = dp2px(context, 50);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = screenHeight - titleBarHeight;
        view.setLayoutParams(layoutParams);
        //设置默认弹起高度
        BottomSheetBehavior behavior = BottomSheetBehavior.from((View) view.getParent());
        int height = screenHeight - titleBarHeight;
        behavior.setPeekHeight(height);
    }
}
