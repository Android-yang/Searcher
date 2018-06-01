package com.android.yangke.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;

import com.android.yangke.R;
import com.android.yangke.base.BaseActivity;
import com.vondear.rxtools.RxWebViewUtils;

import butterknife.BindView;

/**
 * author: yangke on 6/1/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 更多关于作者
 */
public class AuthorActivity extends BaseActivity{

    private static final String URL_AUTHOR = "https://www.jianshu.com/u/eb77504b1d68";

    @BindView(R.id.author_webview)
    WebView mWebView;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_author;
    }

    @Override
    protected void initData() {
        mWebView.loadUrl(URL_AUTHOR);
        RxWebViewUtils.initWebView(this, mWebView);
    }

    @Override
    protected void initView() {
        setTileLeft(getString(R.string.about_author_more));
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //webView 可返回
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
