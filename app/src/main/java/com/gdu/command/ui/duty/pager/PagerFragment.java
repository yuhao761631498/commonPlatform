package com.gdu.command.ui.duty.pager;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.gdu.command.ui.duty.Article;
import com.gdu.command.ui.duty.ArticleAdapter;
import com.gdu.command.ui.duty.base.fragment.BaseFragment;
import com.gdu.command.ui.duty.group.GroupItemDecoration;
import com.gdu.command.ui.duty.group.GroupRecyclerView;
import com.gdu.command.R;

public class PagerFragment extends BaseFragment {

    private GroupRecyclerView mRecyclerView;


    static PagerFragment newInstance() {
        return new PagerFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pager;
    }

    @Override
    protected void initView() {
        mRecyclerView = mRootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());
        mRecyclerView.setAdapter(new ArticleAdapter(mContext));
        mRecyclerView.notifyDataSetChanged();
    }

    @Override
    protected void initData() {

    }

    boolean isScrollTop() {
        return mRecyclerView != null && mRecyclerView.computeVerticalScrollOffset() == 0;
    }
}
