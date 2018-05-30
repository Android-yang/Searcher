package com.android.yangke.activity;

import com.android.yangke.R;
import com.android.yangke.base.BaseActivity;

/**
 * author: yangke on 2018/5/20
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 关于作者
 */
public class AboutAuthorActivity extends BaseActivity {

    @Override
    protected int setLayoutId() { return R.layout.activity_about_author; }

    @Override
    protected void initView() {
        mImmersionBar.statusBarDarkFont(true).init();
        setTileLeft(getString(R.string.about_author));
        setToolbarLineVisible();
    }
}
