package com.android.yangke.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yangke.R;
import com.android.yangke.base.BaseLazyFragment;
import com.android.yangke.tool.Constant;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.RxWebViewTool;
import com.vondear.rxtools.view.RxToast;

import java.util.Random;

import butterknife.BindView;

import static android.content.Context.MODE_PRIVATE;

/**
 * author: yangke on 2018/5/19
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 主fragment
 */
public class HomeFragment extends BaseLazyFragment {
    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.home_hint)
    TextView mTxtHint;
    @BindView(R.id.progressbar_webview)
    ProgressBar mProgressBar;

    //种子引擎地址
    private String[] urls = {"https://btyitao.com", "http://www.btwu.xyz"};

    //QQ
    public static final String QQ = "1551121393";
    //QQ群
    public static final String QQ_FLOCK = "692699158";
    //标识是否是第一次打开软件
    private static final String KEY_FIRST = "ifFirst";

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniView(view);
    }

    private void iniView(View view) {
        iniWebView(view);

        SharedPreferences sp = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        boolean isFirst = sp.getBoolean(KEY_FIRST, true);
        if(isFirst) {
            RxToast.warning("此软件可能包含成人内容，未成年请自觉卸载！", Toast.LENGTH_LONG);
        }
        sp.edit().putBoolean(KEY_FIRST, false).commit();
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
