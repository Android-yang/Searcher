package com.android.yangke.activity;

import com.android.yangke.R;
import com.android.yangke.base.BaseActivity;


/**
 * author: yangke on 2018/11/1
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 网络无法连接
 */
public class NetworkErrorActivity extends BaseActivity {

    @Override
    protected int setLayoutId() {
        return R.layout.activity_network_error;
    }

    @Override
    protected void initView() {
        setTileLeft(getString(R.string.network_no_connection));
    }
}
