package com.gdu.command.ui.organization;

import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.command.R;
import com.gyf.immersionbar.ImmersionBar;

public class OrganizationActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_organitation;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();
    }
}