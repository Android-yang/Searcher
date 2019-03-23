package com.android.yangke.activity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.android.yangke.R;
import com.android.yangke.base.BaseActivity;
import com.android.yangke.base.BaseApplication;
import com.android.yangke.tool.AppTool;
import com.android.yangke.tool.Constant;
import com.android.yangke.tool.ViewTool;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxSPTool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author: yangke on 2018/6/2
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 闪屏页面
 */
public class SplashActivity extends BaseActivity {

    @BindView(R.id.splash_tv_logo)
    TextView mTvLogo;
    @BindView(R.id.splash_txt_skip)
    TextView mTxtSkip;
    private ScheduledExecutorService mSplashTimer;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {
        mSplashTimer = iniSplashImage();
        setSwipeBackEnable(false);
    }

    //闪屏时长
    private int SPLASH_SCREEN_DURATION = AppTool.INSTANCE.getENVIRONMENT_RELEASE() == true ? 3 : 1;
    private ScheduledExecutorService iniSplashImage() {
        ScheduledExecutorService countDownTimer = Executors.newScheduledThreadPool(1);
        countDownTimer.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                SPLASH_SCREEN_DURATION--;
                final int finalDuration = SPLASH_SCREEN_DURATION;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTxtSkip.setText(finalDuration + getString(R.string.btn_skip));
                        if(finalDuration == 0) {
                            toMain();
                        }
                    }
                });
            }
        }, 0, 1, TimeUnit.SECONDS);

        return countDownTimer;
    }


    @Override
    protected void initView() {
        mImmersionBar.statusBarDarkFont(false).init();
        hideTitleBar();
        ViewTool.INSTANCE.textSetTypeface(mTvLogo, BaseApplication.instance(), Constant.INSTANCE.getFONT_FOUNDER_SIMPLIFIED());
    }

    private void toMain() {
        mSplashTimer.shutdown();
        if (RxSPTool.isFirstOpenApp(BaseApplication.instance(), Constant.FIRST_OPEN_APP))
            RxActivityTool.skipActivity(SplashActivity.this, AppExplainActivity.class);
        else {
            RxActivityTool.skipActivity(SplashActivity.this, MainActivity.class);
        }
        overridePendingTransition(R.anim.splash_activity_fade, R.anim.splash_activity_exit);
        finish();
    }

    @OnClick({R.id.splash_txt_skip})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.splash_txt_skip:
                toMain();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
