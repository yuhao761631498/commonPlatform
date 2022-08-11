package com.gdu.baselibrary.base;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.gdu.baselibrary.R;
import com.gdu.baselibrary.utils.DisplayUtil;
import com.gdu.baselibrary.utils.EmptyUtil;
import com.gdu.baselibrary.widget.ColorDividerDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

/**
 * @author by DELL
 * @date on 2017/9/27
 * @describe 通用的列表刷新Fragment
 * 上拉刷新用的是 SmartRefreshLayout
 * 下拉加载用的是 BaseAdapter带的
 * 注意事项：
 * 刷新控件 id必须为：mRefreshLayout
 * RecyclerView id必须为：mRecyclerView
 * 当然也可以自己调整
 */
public abstract class RefreshFragment<P extends IBasePresenter> extends BaseLazyFragment<P> implements
        OnLoadMoreListener {
    /*** 每页请求数量 */
    public int PAGE_COUNT = 10;
    /*** 页码，默认从1开始 */
    protected int PageStart = 1;
    protected int mPage = 1;
    /*** 空布局类型 可以在这里设置默认值 */
    protected int emptyType;
    /*** 是否启用空布局 */
    private boolean enableEmptyView = false;

    protected SmartRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;

    /**
     * 设置刷新框架，需要时调用即可
     */
    protected void initRefreshLayout() {
        mRefreshLayout = (SmartRefreshLayout) getView().findViewById(R.id.mRefreshLayout);
        if (mRefreshLayout != null) {
            mRefreshLayout.setEnableRefresh(true);
            mRefreshLayout.setEnableLoadMore(false);
            mRefreshLayout.setEnableAutoLoadMore(false);
            mRefreshLayout.setDisableContentWhenLoading(true);
            mRefreshLayout.setDisableContentWhenRefresh(true);
            mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(RefreshLayout refreshlayout) {
                    RefreshFragment.this.onRefresh();
                }
            });
        }
    }

    public void setPageCount(int pageCount) {
        PAGE_COUNT = pageCount;
    }

    public void setPageStart(int pageStart) {
        PageStart = pageStart;
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
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.mRecyclerView);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    /**
     * 设置分割线
     *
     * @param id
     */
    protected void addItemDecoration(@ColorRes int id) {
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(new ColorDividerDecoration(ContextCompat.getColor(getActivity(), id)));
        }
    }

    protected void addItemDecoration(@ColorRes int id, int dp) {
        if (mRecyclerView != null) {
            mRecyclerView.addItemDecoration(new ColorDividerDecoration(ContextCompat.getColor(getActivity(), id), DisplayUtil.dip2px(getActivity(), dp)));
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
        if (mRefreshLayout != null) {
            mRefreshLayout.autoRefresh(0);
        }
    }

    /**
     * 结束刷新
     */
    public void finishRefresh() {
        if (mRefreshLayout != null) {
            mRefreshLayout.finishRefresh(0);
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
        this.mAdapter = adapter;
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
        this.mAdapter = adapter;
        this.emptyType = emptyType;
        //是否隐藏头部
        adapter.setHeaderWithEmptyEnable(true);
        //是否隐藏底部
        adapter.setFooterWithEmptyEnable(true);

        finishRefresh();//结束刷新
        if (mPage == PageStart) {
            adapter.setList(list);
            if (EmptyUtil.isEmpty(list)) {
                setEmptyView(adapter);
            }
        } else {
            if (list != null && !list.isEmpty()) {
                adapter.addData(list);
            }
        }

        handleLoadMore(recyclerView, adapter, list);
    }

    public void handleLoadMore(RecyclerView recyclerView, BaseQuickAdapter adapter, List<?> list) {
        if (list == null || list.size() < PAGE_COUNT) {
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

//            View emptyView = View.inflate(getActivity(), R.layout.layout_empty, null);
//            ImageView ivEmpty = (ImageView) emptyView.findViewById(R.id.ivEmpty);
//            TextView tvEmptyHint = (TextView) emptyView.findViewById(R.id.tvEmptyHint);
//
//            String hintDef = "亲，暂未查找到相关数据哦~";
//            int imgResId = R.mipmap.meiyouxinqing;
//            switch (emptyType) {
//                case EmptyConfig.NO_PUBLISH_MEDIA:
//                    imgResId = R.mipmap.meiyoushipin;
//                    hintDef = "您还没有拍摄的视频\n快去开拍吧~";
//                    android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) tvEmptyHint.getLayoutParams();
//                    params.bottomMargin = (int) ((ScreenUtil.getScreenRealHeight(getActivity()) - emptyView.getHeight()) / 2f);
//                    break;
//                case EmptyConfig.NO_LIKE_MEDIA:
//                    imgResId = R.mipmap.meiyoudianzang;
//                    hintDef = "您还没有喜欢的视频\n快去点赞吧~";
//                    params = (android.widget.LinearLayout.LayoutParams) tvEmptyHint.getLayoutParams();
//                    params.bottomMargin = (int) ((ScreenUtil.getScreenRealHeight(getActivity()) - emptyView.getHeight()) / 2f);
//                    break;
//                case EmptyConfig.NO_FOLLOW:
//                    imgResId = R.mipmap.meiyouguanzhu;
//                    hintDef = "您还没有关注的人\n快去关注一下吧~";
//                    break;
//                case EmptyConfig.NO_MOOD:
//                    imgResId = R.mipmap.meiyouxinqing;
//                    hintDef = "您还没有发布的心情\n快去发布一下吧~";
//                    break;
//                case EmptyConfig.NO_CIRCLE:
//                    imgResId = R.mipmap.no_circle;
//                    hintDef = "您当前还没有加入圈子\n快去加入吧~";
//                    break;
//                case EmptyConfig.NO_ORDER:
//                    imgResId = R.mipmap.no_order;
//                    hintDef = "您还没有订单";
//                    break;
//                case EmptyConfig.NO_FOLLOW_ME:
//                    imgResId = R.mipmap.meiyouren_guanzhuwo;
//                    hintDef = "暂时还没有人关注您\n拍摄视频获取关注吧~";
//                    break;
//            }
//
//            ivEmpty.setImageResource(imgResId);
//            tvEmptyHint.setText(hintDef);
//            adapter.setEmptyView(emptyView);
        }
    }

    /**
     * 结束刷新
     */
    @Override
    public void onRequestFinish() {
        super.onRequestFinish();
        finishRefresh();
    }

    public void onRefresh() {
        mPage = 1;
        lazyLoad();
    }

    /**
     * 如需上拉更多 请重写该方法
     */
    @Override
    public void onLoadMore() {
        mPage++;
        lazyLoad();
    }

}
