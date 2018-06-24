package com.android.yangke.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebView;

import com.android.yangke.R;
import com.android.yangke.base.BaseActivity;
import com.android.yangke.fragment.DashboardFragment;
import com.android.yangke.fragment.HomeFragment;
import com.android.yangke.fragment.MeFragment;
import com.android.yangke.http.BaseParam;
import com.android.yangke.http.NetworkTask;
import com.android.yangke.http.Request;
import com.android.yangke.http.ResponseCode;
import com.android.yangke.http.ServiceMap;
import com.android.yangke.listener.OnPageChangeListener_;
import com.android.yangke.service.ApkDownloadService;
import com.android.yangke.view.ViewPagerNoScroller;
import com.android.yangke.vo.AppVersionVo;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.vondear.rxtools.RxDeviceTool;
import com.vondear.rxtools.RxFileTool;
import com.vondear.rxtools.RxPermissionsTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxToast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * author: yangke on 2018/5/19
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 主页面
 */
public class MainActivity extends BaseActivity {

    private static final String APK_NAME = "search.apk";
    private BottomNavigationView mBottomNavigationView;
    private ViewPagerNoScroller mViewPager;
    private HomeFragment mHomeFragment;


    //忽略版本升级
    private static final String KEY_VERSION_IGNORE = "version_ignore";

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        mViewPager = (ViewPagerNoScroller) findViewById(R.id.viewpager);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setSwipeBackEnable(false);

        checkVersionCode();
        RxToast.normal("-----------");
        RxPermissionsTool
                .with(this)
                .addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .initPermission();
    }


    private void checkVersionCode() {
        Request.startRequest(new BaseParam() {
            @Override
            public Map<String, String> toGetParamMap() {
                return new HashMap<>();
            }

            @Override
            protected String url() {
                return "/version";
            }
        }, ServiceMap.CHECK_APP_VERSION, this);
    }

    @Override
    public void onRequestSuccess(NetworkTask task) {
        super.onRequestSuccess(task);
        switch (task.key) {
            case CHECK_APP_VERSION:
                final AppVersionVo vo = (AppVersionVo) task.response;
                if (vo.mStatus == ResponseCode.CODE_200) {
                    showUpdateAppDialog(vo);
                } else {
                    RxToast.warning(vo.mMessage);
                }
                break;
        }
    }

    private void showUpdateAppDialog(AppVersionVo vo) {
        final AppVersionVo.AppDataBean versionData = vo.mData;
        if (RxDeviceTool.getAppVersionCode(this) < versionData.mVersion) {
            int ignoreVersion = RxSPTool.getInt(getApplicationContext(), KEY_VERSION_IGNORE);
            if (ignoreVersion == versionData.mVersion) {
                return;
            }
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            AlertDialog dialog = builder
                    .setCancelable(false)
                    .setTitle("我们更新了新版，快来体验！")
                    .setMessage(vo.mMessage)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (RxFileTool.sdCardIsAvailable()) {
                                Intent it = new Intent(MainActivity.this, ApkDownloadService.class);
                                it.putExtra(ApkDownloadService.KEY_APK_URL, versionData.mUrl);
                                it.putExtra(ApkDownloadService.KEY_APK_NAME, APK_NAME);
                                String apkFilePath = getExternalFilesDir("search").getAbsolutePath();
                                it.putExtra(ApkDownloadService.KEY_APK_FILE_PATH, apkFilePath);
                                startService(it);
                            } else {
                                RxToast.warning("SD 不可用");
                            }
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setNeutralButton("忽略此版本", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RxSPTool.putInt(getApplicationContext(), KEY_VERSION_IGNORE, versionData.mVersion);
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    @Override
    protected void initView() {
        hideTitleBar();
        statusBarDarkFont(false);
        setupViewPager(mViewPager);
    }

    private void statusBarDarkFont(boolean b) {
        mImmersionBar.statusBarDarkFont(b).init();
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new OnPageChangeListener_() {
            @Override
            public void onPageSelected(int i) {
                MenuItem menuItem = mBottomNavigationView.getMenu().getItem(i);
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    mBottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
            }
        });

        viewPager.setOffscreenPageLimit(3);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        DashboardFragment dashBoardFragment = new DashboardFragment();
        mHomeFragment = new HomeFragment();
        MeFragment meFragment = new MeFragment();
        adapter.addFragment(dashBoardFragment);
        adapter.addFragment(mHomeFragment);
        adapter.addFragment(meFragment);
        viewPager.setAdapter(adapter);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    mViewPager.setCurrentItem(0, false);
                    statusBarDarkFont(false);
                    return true;

                case R.id.navigation_home:
                    mViewPager.setCurrentItem(1, false);
                    statusBarDarkFont(false);
                    return true;

                case R.id.navigation_notifications:
                    mViewPager.setCurrentItem(2, false);
                    statusBarDarkFont(true);
                    return true;
            }
            return false;
        }
    };

    public void getFile(String url, final String filePath, String name) {
        OkGo.get(url)//
                .tag(this)//
                .execute(new FileCallback(filePath, name) {  //文件下载时，可以指定下载的文件目录和文件名
                    @Override
                    public void onSuccess(File file, Call call, okhttp3.Response response) {
                        // file 即为文件数据，文件保存在指定目录
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), "application/vnd.android.package-archive");
                        startActivity(i);
                    }

                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调下载进度(该回调在主线程,可以直接更新ui)

                    }
                });
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList == null ? 0 : mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WebView homeFragmentWebView = mHomeFragment.getWebView();
        if (homeFragmentWebView != null && keyCode == KeyEvent.KEYCODE_BACK && homeFragmentWebView.canGoBack()) {
            //webView 可返回
            homeFragmentWebView.goBack();
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (RxToast.doubleClickExit()) {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        Intent it = new Intent(this, ApkDownloadService.class);
        stopService(it);
        super.onDestroy();
    }
}
