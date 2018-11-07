package com.android.yangke.activity;

import android.os.CountDownTimer;
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
    private CountDownTimer mSkipTimer;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {
        mSkipTimer = iniSplashImage();
        setSwipeBackEnable(false);
    }

    private CountDownTimer iniSplashImage() {
        CountDownTimer timer = new CountDownTimer(AppTool.INSTANCE.getSPLASH_SCREEN_DURATION(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long second = millisUntilFinished / 1000;
                mTxtSkip.setText(second + getString(R.string.btn_skip));
            }

            @Override
            public void onFinish() {
                toMain();
            }
        };
        timer.start();
        return timer;
    }


    @Override
    protected void initView() {
        mImmersionBar.statusBarDarkFont(false).init();
        hideTitleBar();
        ViewTool.INSTANCE.textSetTypeface(mTvLogo, BaseApplication.instance(), Constant.INSTANCE.getFONT_FOUNDER_SIMPLIFIED());
    }

    private void toMain() {
        if (RxSPTool.isFirstOpenApp(BaseApplication.instance(), Constant.FIRST_OPEN_APP))
            RxActivityTool.skipActivity(SplashActivity.this, AppExplainActivity.class);
        else {
            RxActivityTool.skipActivity(SplashActivity.this, MainActivity.class);
        }
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSkipTimer.cancel();
        finish();
    }

    @OnClick({R.id.splash_txt_skip})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.splash_txt_skip:
                mSkipTimer.cancel();
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
