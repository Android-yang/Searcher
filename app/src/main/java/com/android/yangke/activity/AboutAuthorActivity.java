package com.android.yangke.activity;

import com.android.yangke.R;
import com.android.yangke.base.BaseActivity;
import com.vondear.rxtools.RxActivityTool;

/**
 * author: yangke on 2018/5/20
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 关于作者
 */
public class AboutAuthorActivity extends BaseActivity {

    @Override
    protected int setLayoutId() {
        return R.layout.activity_about_author;
    }

    @Override
    protected void initView() {
        setTileLeft(getString(R.string.about_author));
        setTitleRight(getString(R.string.about_author_more));
        setToolbarLineVisible();
    }

    @Override
    protected void onRightButtonClick() {
        RxActivityTool.skipActivity(this, AboutAuthorMoreActivity.class);
    }
}
