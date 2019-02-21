package com.android.yangke.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.yangke.R;
import com.android.yangke.base.BaseApplication;
import com.android.yangke.base.BaseLazyFragment;
import com.android.yangke.tool.Constant;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.RxWebViewTool;
import com.vondear.rxtools.view.RxToast;

import java.util.Random;

import butterknife.BindView;


/**
 * author: yangke on 2018/5/19
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 主fragment
 */
public class VipFragment extends BaseLazyFragment {

    @BindView(R.id.webView) WebView mWebView;
    @BindView(R.id.home_hint) TextView mTxtHint;
    @BindView(R.id.progressbar_webview) ProgressBar mProgressBar;

    private String[] urls = {"https://btyitao.com", "http://www.btwu.xyz"}; //种子引擎地址
    public static final String QQ = "QQ：1551121393";     //QQ
    public static final String QQ_FLOCK = "692699158";//QQ群

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_vip;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniView(view);
    }

    private void iniView(View view) {
        iniWebView(view);

        if (RxSPTool.isFirstOpenApp(BaseApplication.instance(), Constant.FIRST_OPEN_INDICATE)) {
            RxToast.warning(getString(R.string.toast_warm_adult_software));
        }
    }

    private void iniWebView(View v) {
        RxWebViewTool.initWebView(getContext(), mWebView, mProgressBar);
        Random r = new Random();
        int index = r.nextInt(urls.length);
        String url = urls[index];
        boolean vip = RxSPTool.getBoolean(getContext(), Constant.INSTANCE.getKEY_VIP());
        if(vip) {//默认非VIP，VIP处理下
            mTxtHint.setVisibility(View.GONE);
            mWebView.loadUrl(url);
        }
    }

    public WebView getWebView() {
        return mWebView;
    }
}
