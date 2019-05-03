package com.vondear.rxtools;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.vondear.rxtools.view.RxToast;

/**
 * @author Vondear
 * @date 2017/4/1
 */

public class RxWebViewTool {

    private static final String KEY_CURRENT_URL = "current_url";      //当期页面URL
    private static final String KEY_CURRENT_X_POSITION = "x_position";//当期webView滚动的x位置
    private static final String KEY_CURRENT_Y_POSITION = "y_position";//当期webView滚动的y位置

    public static void initWebView(final Context context, final WebView webView, final ProgressBar progressBar) {
        final WebSettings webSettings = webView.getSettings();
        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//加载缓存否则网络
        }

        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setLoadsImagesAutomatically(true);//图片自动缩放 打开
        } else {
            webSettings.setLoadsImagesAutomatically(false);//图片自动缩放 关闭
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//软件解码
        }
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);//硬件解码

//        webSettings.setAllowContentAccess(true);
//        webSettings.setAllowFileAccessFromFileURLs(true);
//        webSettings.setAppCacheEnabled(true);
   /*     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }*/


        // setMediaPlaybackRequiresUserGesture(boolean require) //是否需要用户手势来播放Media，默认true

        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
//        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setSupportZoom(true);// 设置可以支持缩放
        webSettings.setBuiltInZoomControls(true);// 设置出现缩放工具 是否使用WebView内置的缩放组件，由浮动在窗口上的缩放控制和手势缩放控制组成，默认false

        webSettings.setDisplayZoomControls(false);//隐藏缩放工具
        webSettings.setUseWideViewPort(true);// 扩大比例的缩放

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setDatabaseEnabled(true);//
        webSettings.setSavePassword(true);//保存密码
        webSettings.setDomStorageEnabled(true);//是否开启本地DOM存储  鉴于它的安全特性（任何人都能读取到它，尽管有相应的限制，将敏感数据存储在这里依然不是明智之举），Android 默认是关闭该功能的。
        webView.setSaveEnabled(true);
        webView.setKeepScreenOn(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            configScrollYPosition(context, webView);//配置webview当期滚动的y位置
        }

        progressBar.setMax(100);

        //设置此方法可在WebView中打开链接，反之用浏览器打开
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setMax(newProgress);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("magnet:?xt") || url.contains("thunder:")) {
                    if (!RxAppTool.appIsInstalled(context, "com.xunlei.downloadprovider", null)) {
                        RxToast.normal("迅雷没有安装或版本过低，链接已复制到剪切板");
                        return true;
                    }

                    RxClipboardTool.copyText(context, url);
                    RxActivityTool.action2Thunder(context);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (RxPayTool.isPay(context)) {
                    RxToast.error("免费次数已经用完");
                    return;
                }
                progressBar.setVisibility(View.GONE);
                if (!webView.getSettings().getLoadsImagesAutomatically()) {
                    webView.getSettings().setLoadsImagesAutomatically(true);
                }
                if (url.equals(RxSPTool.getString(context, KEY_CURRENT_URL))) {
                    webView.setScrollX(RxSPTool.getInt(context, KEY_CURRENT_X_POSITION));
                    webView.setScrollY(RxSPTool.getInt(context, KEY_CURRENT_Y_POSITION));
                }
            }
        });
        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String paramAnonymousString1, String paramAnonymousString2,
                                        String paramAnonymousString3, String paramAnonymousString4,
                                        long paramAnonymousLong) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(paramAnonymousString1));
                context.startActivity(intent);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void configScrollYPosition(final Context context, final WebView webView) {
        webView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                RxSPTool.putInt(context, KEY_CURRENT_X_POSITION, scrollX);
                RxSPTool.putInt(context, KEY_CURRENT_Y_POSITION, scrollY);
                RxSPTool.putString(context, KEY_CURRENT_URL, webView.getUrl());
            }
        });
    }

    public static void loadData(WebView webView, String content) {
        webView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);//这种写法可以正确解码
    }
}
