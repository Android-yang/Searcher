package com.android.yangke.fragment;


import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.yangke.R;
import com.android.yangke.base.BaseLazyFragment;
import com.vondear.rxtools.RxClipboardTool;

import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

/**
 * author: yangke on 2018/5/19
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 主fragment
 */
public class HomeFragment extends BaseLazyFragment implements View.OnClickListener {
    private WebView mWebView;

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
        view.findViewById(R.id.author).setOnClickListener(this);
        view.findViewById(R.id.red_money).setOnClickListener(this);

//        iniHintDialog();
        iniWebView(view);


        SharedPreferences sp = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        boolean isFirst = sp.getBoolean(KEY_FIRST, true);
//        if(isFirst) {
//            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
//            dialog.setMessage("此软件可谓无所不能，场面比较血腥，18岁以下误入！！！\n" +
//                    "会员电影？优衣库视频？皮皮鳝？一切皆有可能，使用此软件建议你配合迅雷使用效果更佳...")
//                    .show();
//        }
        sp.edit().putBoolean(KEY_FIRST, false).commit();
    }

    private void iniWebView(View v) {
        mWebView = v.findViewById(R.id.webView);
        iniWebSetting(mWebView);
        iniWebview();
    }

    private void iniHintDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setMessage("使用本软件前一定认真阅读本信息！\n本软件所展示内容均来自于互联网搜索，仅用于自己学习进步。同时本软件不做任何存储操作，" +
                "如涉及商业信息或侵害了您的利益请及时 QQ 联系（" + QQ + "）本人，我会第一时间清除该软件。最后，你可以添加 QQ 群（" + QQ_FLOCK + "）" +
                "提出你的对软件的使用感觉，我会第一时间修复问题让软加更加好用，当然此群收费（3-5元）。一位集美貌与才华的程序员...")
                .show();
    }

    private void iniWebview() {
        Random r = new Random();
        int index = r.nextInt(urls.length);
        String url = urls[index];

        mWebView.loadUrl(url);

        //设置此方法可在WebView中打开链接，反之用浏览器打开
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                    return false;
                }

                // Otherwise allow the OS to handle things like tel, mailto, etc.
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                Log.i("-->", "地址：" + url);
                url = url.toLowerCase();

                if (url.contains("http://www.btwu.xyz") || url.contains("https://btyitao.com")) {
                    return super.shouldInterceptRequest(view, url);
                } else {
                    return new WebResourceResponse(null, null, null);
                }
            }
        });
    }

    protected void iniWebSetting(WebView webView) {
        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setSupportZoom(true);// 设置可以支持缩放
        webSetting.setBuiltInZoomControls(true);// 设置出现缩放工具 是否使用WebView内置的缩放组件，由浮动在窗口上的缩放控制和手势缩放控制组成，默认false
        webSetting.setDisplayZoomControls(false);//隐藏缩放工具
        webSetting.setUseWideViewPort(true);// 扩大比例的缩放
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setBlockNetworkImage(false);//设置自动加载图片
        webSetting.setDatabaseEnabled(true);//
        webSetting.setSavePassword(true);//保存密码
        webSetting.setDomStorageEnabled(true);//是否开启本地DOM存储  鉴于它的安全特性（任何人都能读取到它，尽管有相应的限制，将敏感数据存储在这里依然不是明智之举），Android 默认是关闭该功能的。
        webSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//加载缓存否则网络

        if (Build.VERSION.SDK_INT >= 19) {
            webSetting.setLoadsImagesAutomatically(true);//图片自动缩放 打开
        } else {
            webSetting.setLoadsImagesAutomatically(false);//图片自动缩放 关闭
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.author:
                showMsg(QQ);
                break;
            case R.id.red_money:
                showMsg(QQ_FLOCK);
                break;
        }
    }

    private void showMsg(String str) {
        RxClipboardTool.copyText(getContext(), str);
        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }
}
