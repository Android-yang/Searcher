package com.android.yangke.activity;

import com.android.yangke.R;
import com.android.yangke.base.BaseActivity;

/**
 * author: yangke on 2018/5/20
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 关于我们
 */
public class AboutWeActivity extends BaseActivity {

    @Override
    protected int setLayoutId() { return R.layout.activity_about_we; }

    @Override
    protected void initView() {
        mImmersionBar.statusBarDarkFont(true).init();
        setTileLeft("个人中心");
    }
}
