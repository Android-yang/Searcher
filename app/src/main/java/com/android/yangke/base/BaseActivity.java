package com.android.yangke.base;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.yangke.BuildConfig;
import com.android.yangke.tool.AppTool;
import com.android.yangke.tool.receiver.NetBroadcastReceiver;
import com.gyf.barlibrary.ImmersionBar;
import com.vondear.rxtools.view.RxToast;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * author: yangke on 2018/5/19
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : base activity
 */
public abstract class BaseActivity extends AbsActivity implements NetBroadcastReceiver.NetChangeListener{

    private Unbinder mBinder;
    private InputMethodManager mInputMethodManager;
    protected ImmersionBar mImmersionBar;

    private NetBroadcastReceiver mNetReceiver;
    public static NetBroadcastReceiver.NetChangeListener mListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutId());
        if(!BuildConfig.ENVIRONMENT) {
            RxToast.showToast(getClass().getSimpleName());
            Log.d(AppTool.INSTANCE.getTAG(), getClass().getSimpleName());
        }
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
        mListener = this;
        mNetReceiver = new NetBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetReceiver, intentFilter);
    }

    protected void initView() {
    }

    protected void setListener() {
    }

    @Override
    public void onNetworkChangeListener(int status) {
    }

    @Override
    public void finish() {
        super.finish();
        hideSoftKeyBoard();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        if(mNetReceiver != null) {
            unregisterReceiver(mNetReceiver);
        }
    }

    @Override
    protected void onHandleMessage(Message msg) { }
}

