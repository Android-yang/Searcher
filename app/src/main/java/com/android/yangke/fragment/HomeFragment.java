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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.yangke.R;
import com.android.yangke.base.BaseLazyFragment;
import com.vondear.rxtools.RxClipboardTool;
import com.vondear.rxtools.RxWebViewTool;

import java.util.Random;

import butterknife.BindView;

import static android.content.Context.MODE_PRIVATE;

/**
 * author: yangke on 2018/5/19
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 主fragment
 */
public class HomeFragment extends BaseLazyFragment implements View.OnClickListener {
    @BindView(R.id.webView)
    WebView mWebView;
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
        RxWebViewTool.initWebView(getContext(), mWebView, mProgressBar);
        Random r = new Random();
        int index = r.nextInt(urls.length);
        String url = urls[index];

        mWebView.loadUrl(url);
    }

    private void iniHintDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setMessage("使用本软件前一定认真阅读本信息！\n本软件所展示内容均来自于互联网搜索，仅用于自己学习进步。同时本软件不做任何存储操作，" +
                "如涉及商业信息或侵害了您的利益请及时 QQ 联系（" + QQ + "）本人，我会第一时间清除该软件。最后，你可以添加 QQ 群（" + QQ_FLOCK + "）" +
                "提出你的对软件的使用感觉，我会第一时间修复问题让软加更加好用，当然此群收费（3-5元）。一位集美貌与才华的程序员...")
                .show();
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
