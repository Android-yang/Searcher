package com.android.yangke.activity;

import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.android.yangke.R;
import com.android.yangke.base.BaseActivity;
import com.vondear.rxtools.RxWebViewTool;

import butterknife.BindView;


/**
 * author: yangke on 6/15/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 个人中心
 */
public class AuthorActivity extends BaseActivity {
    private static final String URL_AUTHOR = "https://www.jianshu.com/p/3a17db598e57";

    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.progressbar_webview)
    ProgressBar mProgressBatTop;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_author;
    }

    @Override
    protected void initData() {
        RxWebViewTool.initWebView(this, mWebView, mProgressBatTop);
        mWebView.loadUrl(URL_AUTHOR);
    }

    @Override
    protected void initView() {
        setTileLeft(getString(R.string.title_personal_center));
        setToolbarLineVisible();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //webView 可返回
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
