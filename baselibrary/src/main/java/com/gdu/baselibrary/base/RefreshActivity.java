package com.gdu.baselibrary.base;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gdu.baselibrary.R;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.DisplayUtil;
import com.gdu.baselibrary.utils.EmptyUtil;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.baselibrary.widget.ColorDividerDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

/**
 * @author by DELL
 * @date on 2017/9/27
 * @describe 通用的列表刷新Activity
 * <p>
 * 上拉刷新用的是 SmartRefreshLayout
 * 下拉加载用的是 BaseAdapter带的
 * 注意事项：
 * 刷新控件 id必须为：mRefreshLayout
 * RecyclerView id必须为：mRecyclerView
 * 当然也可以自己调整
 */
public abstract class RefreshActivity<P extends IBasePresenter> extends BaseActivity<P>
        implements com.chad.library.adapter.base.listener.OnLoadMoreListener {
    /*** 每页请求数量 */
    public int PAGE_COUNT = 10;
    /*** 页码，默认从1开始 */
    protected int PageStart = 1;
    protected int mPage = 1;
    /*** 空布局类型 可以在这里设置默认值 */
    protected int emptyType = -1;
    /*** 是否启用空布局 */
    private boolean enableEmptyView = false;
    /** 是否已经加载完了，没有更多数据了 */
    protected boolean isLoadEnd;

    protected SmartRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    /**
     * 设置刷新框架，需要时调用即可
     */
    protected void initRefreshLayout() {
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.mRefreshLayout);
        if (mRefreshLayout != null) {
            mRefreshLayout.setEnableRefresh(true);
            mRefreshLayout.setEnableLoadMore(false);
            mRefreshLayout.setEnableAutoLoadMore(false);
            mRefreshLayout.setDisableContentWhenLoading(true);
            mRefreshLayout.setDisableContentWhenRefresh(true);
            mRefreshLayout.setOnRefreshListener(refreshlayout -> RefreshActivity.this.onRefresh());
            mRefreshLayout.setOnLoadMoreListener(refreshLayout -> loadMoreHandle());
        }
    }

    /**
     * 开关刷新
     *
     * @param enableRefresh
     */
    public void setEnableRefresh(boolean enableRefresh) {
        if (mRefreshLayout != null) {
            mRefreshLayout.setEnableRefresh(enableRefresh);
        }
    }

    /**
     * 初始化列表控件
     */
    protected void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
    }

    public void setPageCount(int pageCount) {
        PAGE_COUNT = pageCount;
    }

    public void setPageStart(int pageStart) {
        PageStart = pageStart;
    }

    /**
     * 设置分割线
     *
     * @param colorId
     */
    protected void addItemDecoration(@ColorRes int colorId) {
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(new ColorDividerDecoration(ContextCompat.getColor(this, colorId)));
        }
    }

    protected void addItemDecoration(@ColorRes int colorId, int dp) {
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(new ColorDividerDecoration(ContextCompat.getColor(getActivity(), colorId), DisplayUtil.dip2px(getActivity(), dp)));
        }
    }

    protected void addItemDecoration() {
        //设置默认分割线
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(new ColorDividerDecoration(Color.parseColor("#CCCCCC")));
        }
    }


    /**
     * 自动刷新
     */
    public void autoRefresh() {
        MyLogUtils.i("autoRefresh()");
        if (mRefreshLayout != null) {
            mRefreshLayout.autoRefresh(0);
        }
    }

    /**
     * 结束刷新
     */
    public void finishRefresh() {
        MyLogUtils.i("finishRefresh()");
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.finishRefresh();
        }
    }

    /***
     * 是否启用空布局
     * @param enableEmptyView
     */
    public void setEnableEmptyView(boolean enableEmptyView) {
        this.enableEmptyView = enableEmptyView;
    }

    public void setEnableEmptyView(boolean enableEmptyView, int emptyType) {
        this.enableEmptyView = enableEmptyView;
        this.emptyType = emptyType;
    }


    public void setLoadData(BaseQuickAdapter adapter, List<?> list) {
        if (CommonUtils.isEmptyList(list)) {
            MyLogUtils.i("setLoadData() adapter listSize = 0");
        } else {
            MyLogUtils.i("setLoadData() adapter listSize = " + list.size());
        }
        //emptyType 可以给一个默认值,作为统一设置
        setLoadData(adapter, list, emptyType);
    }

    /**
     * 不分页的 设置数据
     *
     * @param adapter
     * @param list
     * @param emptyType
     */
    public void setLoadData(BaseQuickAdapter adapter, List<?> list, int emptyType) {
        this.emptyType = emptyType;
        //是否隐藏头部
        adapter.setHeaderWithEmptyEnable(true);
        //是否隐藏底部
        adapter.setFooterWithEmptyEnable(true);

        finishRefresh();
        adapter.setList(list);
        if (EmptyUtil.isEmpty(list)) {
            setEmptyView(adapter);
        }
    }


    public void setLoadMore(BaseQuickAdapter adapter, List<?> list) {
        setLoadMore(adapter, list, emptyType);
    }

    /**
     * 如果使用的是当前的 mRecyclerView
     *
     * @param adapter
     * @param list
     * @param emptyType
     */
    public void setLoadMore(BaseQuickAdapter adapter, List<?> list, int emptyType) {
        if (mRecyclerView != null) {
            setLoadMore(mRecyclerView, adapter, list, emptyType);
        }
    }


    /**
     * 结束刷新
     * 自动设置上拉更多
     *
     * @param adapter
     * @param list
     */
    public void setLoadMore(RecyclerView recyclerView, BaseQuickAdapter adapter, List<?> list, int emptyType) {
        this.emptyType = emptyType;
        //是否隐藏头部
        adapter.setHeaderWithEmptyEnable(true);
        //是否隐藏底部
        adapter.setFooterWithEmptyEnable(true);

        finishRefresh();//结束刷新
        if (mPage == PageStart) {
            isLoadEnd = false;
            adapter.setList(list);
            if (EmptyUtil.isEmpty(list)) {
                isLoadEnd = true;
                setEmptyView(adapter);
            }
        } else {
            if (list != null && !list.isEmpty()) {
                adapter.addData(list);
            }
        }

        if (list == null || list.size() < PAGE_COUNT) {
            isLoadEnd = true;
            adapter.getLoadMoreModule().loadMoreEnd();
        } else {
            adapter.getLoadMoreModule().setEnableLoadMore(true);
            adapter.getLoadMoreModule().setOnLoadMoreListener(this);
            adapter.getLoadMoreModule().loadMoreComplete();
        }
    }

    /**
     * 设置空状态
     * <p>
     * 根据项目情况 自由定制
     *
     * @param adapter
     */
    protected void setEmptyView(BaseQuickAdapter adapter) {
        if (enableEmptyView) {

        }
    }


    /**
     * 结束刷新
     */
    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        MyLogUtils.i("onRequestFinish()");
        finishRefresh();
    }

    public void onRefresh() {
        MyLogUtils.i("onRefresh()");
        mPage = 1;
        loadData();
    }

    /**
     * 如需上拉更多 请重写该方法
     */
    @Override
    public void onLoadMore() {
        MyLogUtils.i("onLoadMore()");
        loadMoreHandle();
    }

    private void loadMoreHandle() {
        MyLogUtils.i("loadMoreHandle()");
        mPage++;
//        loadData();
    }


    public abstract void loadData();

}
