package com.android.yangke.activity;

import com.android.yangke.R;
import com.android.yangke.base.BaseActivity;

/**
 * author: yangke on 2018/5/28.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 软件必读
 */
public class SoftwareRequiredActivity extends BaseActivity {

    @Override
    protected int setLayoutId() {
        return R.layout.activity_software_required;
    }

    @Override
    protected void initView() {
        setTileLeft(getString(R.string.me_software_required));
        setToolbarLineVisible();
    }
}
