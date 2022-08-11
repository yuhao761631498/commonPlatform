package com.gdu.command.ui.cases.deal;

import android.widget.ListView;

import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.command.R;
import com.gdu.model.config.GlobalVariable;
import com.gyf.immersionbar.ImmersionBar;

/**
 * 批示列表
 */
public class CommentsActivity extends BaseActivity {

    private ListView mCommentsListView;
    private CaseCommentsAdapter mCaseCommentsAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_comments;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();
        initViews();
        initData();
    }

    private void initViews() {
        mCommentsListView = findViewById(R.id.case_comments_listview);
        mCaseCommentsAdapter = new CaseCommentsAdapter(mContext);
        mCommentsListView.setAdapter(mCaseCommentsAdapter);
    }

    private void initData() {
        mCaseCommentsAdapter.setData(GlobalVariable.sCommentInfoList);
    }
}
