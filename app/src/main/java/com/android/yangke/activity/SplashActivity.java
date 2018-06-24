package com.android.yangke.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.TextView;

import com.android.yangke.R;
import com.android.yangke.base.BaseActivity;
import com.android.yangke.util.AppTools;
import com.vondear.rxtools.RxActivityTool;

import butterknife.BindView;

/**
 * author: yangke on 2018/6/2
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 闪屏页面
 */
public class SplashActivity extends BaseActivity {

    @BindView(R.id.splash_tv_logo)
    TextView mTvLogo;

    /**
     * TODO 如果项目使用的字体比较多，此函数可以封装在工具类中
     *
     * @param textView textView
     * @param ctx      context
     * @param fontName 字体名字，字体文件放在 assets 根目录。例：方正字体
     */
    private static void textSetTypeface(TextView textView, Context ctx, String fontName) {
        Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), fontName);
        textView.setTypeface(typeface);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {
        iniSplashScreenImage();
        setSwipeBackEnable(false);
    }

    @Override
    protected void initView() {
        mImmersionBar.statusBarDarkFont(false).init();
        hideTitleBar();
        textSetTypeface(mTvLogo, this, "方正启体简体.ttf");
    }

    /**
     * 初始化闪屏图片
     */
    private void iniSplashScreenImage() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                toMain();
            }
        }, AppTools.SPLASH_SCREEN_DURATION);
    }

    private void toMain() {
        RxActivityTool.skipActivity(SplashActivity.this, MainActivity.class);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
