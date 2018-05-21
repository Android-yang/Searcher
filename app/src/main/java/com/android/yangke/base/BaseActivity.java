package com.android.yangke.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author: yangke on 2018/5/19
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : base activity
 */
public abstract class BaseActivity extends AbsActivity {

    private Unbinder mBinder;
    private InputMethodManager mInputMethodManager;
    protected ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutId());
        mImmersionBar = ImmersionBar.with(this).statusBarDarkFont(true);
        mImmersionBar.init();
        //绑定控件
        mBinder = ButterKnife.bind(this);

        //初始化数据
        initData();
        //view与数据绑定
        initView();
        //设置监听
        setListener();
    }

    protected abstract int setLayoutId();

    protected void initData() {
    }

    protected void initView() {
    }

    protected void setListener() {
    }

    @Override
    public void finish() {
        super.finish();
        hideSoftKeyBoard();
    }

    /**
     * 隐藏输入法
     */
    public void hideSoftKeyBoard() {
        View localView = getCurrentFocus();
        if (this.mInputMethodManager == null) {
            mInputMethodManager = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.mInputMethodManager != null)) {
            this.mInputMethodManager.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null) {
            mImmersionBar.destroy();  //在BaseActivity里销毁
        }
        if (mBinder != null) {
            mBinder.unbind();
        }
    }
}

