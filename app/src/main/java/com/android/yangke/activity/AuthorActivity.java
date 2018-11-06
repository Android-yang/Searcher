package com.android.yangke.activity;

import android.Manifest;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.android.yangke.R;
import com.android.yangke.base.BaseActivity;
import com.vondear.rxtools.RxFileTool;
import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.RxPermissionsTool;
import com.vondear.rxtools.RxWebViewTool;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogScaleView;
import com.vondear.rxtools.view.scaleimage.RxScaleImageView;

import java.io.File;
import java.io.IOException;

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
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_qr_code, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_my_qr_code) {
            showQrCodeView();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showQrCodeView() {
        RxDialogScaleView dialog = new RxDialogScaleView(this);
        dialog.setImage(R.drawable.my_qr_code);
        RxScaleImageView imgQrCode = dialog.getRxScaleImageView();
        dialog.getWindow().setWindowAnimations(R.style.DialogEnterOut4);
        imgQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, getString(R.string.long_click_save_qr_code), Snackbar.LENGTH_SHORT).show();
            }
        });
        imgQrCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                RxPermissionsTool
                        .with(AuthorActivity.this)
                        .addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .initPermission();
                if (!RxPermissionsTool.permissionGranted(AuthorActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    return false;
                }

                Drawable drawable = getResources().getDrawable(R.drawable.my_qr_code);
                //TODO 如果图片过大后期考虑开现场进行处理
                try {
                    String sdcardPath = RxFileTool.getSDCardPath();
                    String sdcardPicturesPath = sdcardPath + "Pictures" + File.separator;
                    File file = new File(sdcardPicturesPath);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    String imageLocation = sdcardPicturesPath + System.currentTimeMillis() + ".jpg";
                    RxFileTool.saveBitmap2Sdcard(RxImageTool.drawable2Bitmap(drawable), imageLocation);
                    RxToast.success(imageLocation);
                } catch (IOException e) {
                    e.printStackTrace();
                    RxToast.error("图片保存失败");
                }
                return false;
            }
        });
        dialog.show();
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
