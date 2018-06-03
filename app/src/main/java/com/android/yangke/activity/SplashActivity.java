package com.android.yangke.activity;

import android.os.Handler;
import android.view.KeyEvent;

import com.android.yangke.R;
import com.android.yangke.base.BaseActivity;
import com.android.yangke.util.AppTools;
import com.vondear.rxtools.RxActivityUtils;

/**
 * author: yangke on 2018/6/2
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 闪屏页面
 */
public class SplashActivity extends BaseActivity {


    @Override
    protected int setLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {
        iniSplashScreenImage();
    }

    @Override
    protected void initView() {
        mImmersionBar.statusBarDarkFont(false).init();
        hideTitleBar();
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
        RxActivityUtils.skipActivity(SplashActivity.this, MainActivity.class);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 例子
     * 下载APk文件并自动弹出安装
     */
/*    public void getFile(String url, final String filePath, String name) {
        OkGo.get(url)//
                .tag(this)//
                .execute(new FileCallback(filePath, name) {  //文件下载时，可以指定下载的文件目录和文件名
                    @Override
                    public void onSuccess(File file, Call call, Response response) {
                        // file 即为文件数据，文件保存在指定目录
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), "application/vnd.android.package-archive");
                        context.startActivity(i);
                    }
                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调下载进度(该回调在主线程,可以直接更新ui)
                    }
                });
    }*/

}
