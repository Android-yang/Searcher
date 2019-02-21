package com.android.yangke.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
<<<<<<< HEAD
=======
import android.os.Build;
>>>>>>> yangkeDevelopment
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;

import com.android.yangke.R;
import com.android.yangke.base.BaseActivity;
import com.android.yangke.fragment.MeFragment;
import com.android.yangke.fragment.SearchFragment;
import com.android.yangke.fragment.VipFragment;
import com.android.yangke.http.BaseParam;
import com.android.yangke.http.NetworkTask;
import com.android.yangke.http.Request;
import com.android.yangke.http.ResponseCode;
import com.android.yangke.http.ServiceMap;
import com.android.yangke.listener.OnPageChangeListener_;
import com.android.yangke.service.ApkDownloadService;
import com.android.yangke.tool.ViewTool;
import com.android.yangke.view.ViewPagerNoScroller;
import com.android.yangke.vo.AppVersionVo;
<<<<<<< HEAD
=======
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxBarTool;
>>>>>>> yangkeDevelopment
import com.vondear.rxtools.RxDeviceTool;
import com.vondear.rxtools.RxFileTool;
import com.vondear.rxtools.RxNetTool;
import com.vondear.rxtools.RxPermissionsTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: yangke on 2018/5/19
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 主页面
 */
public class MainActivity extends BaseActivity {

<<<<<<< HEAD
    public static final String KEY_DOWNLOAD_APK_URL = "key_download_apk_url";
    private static final String APK_NAME = "search.apk";
    //忽略版本升级
    private static final String KEY_VERSION_IGNORE = "version_ignore";
    private BottomNavigationView mBottomNavigationView;
    private ViewPagerNoScroller mViewPager;
    private HomeFragment mHomeFragment;
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
=======
    private BottomNavigationView mBottomNavigationView;
    private ViewPagerNoScroller mViewPager;
    private VipFragment mVipFragment;
    private View mNoNetwork;

    //忽略版本升级
    private static final String KEY_VERSION_IGNORE = "version_ignore";
    private static final String APK_NAME = "search.apk";
    private static final int NO_NETWORK_ANIMATION_DURATION = 700;
    private SearchFragment mSearchFragment;

>>>>>>> yangkeDevelopment

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initData() {
        super.initData();
        mViewPager = (ViewPagerNoScroller) findViewById(R.id.viewpager);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        ViewStub viewStubNoNetwork = (ViewStub) findViewById(R.id.viewStub_no_network);
        mNoNetwork = viewStubNoNetwork.inflate();
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        setSwipeBackEnable(false);

        checkVersionCode();
        RxPermissionsTool
                .with(this)
                .addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .initPermission();
    }

<<<<<<< HEAD
=======
    private void handleNoNetwork() {
        if (RxNetTool.isAvailable(this)) {
            if (mNoNetwork.getVisibility() != View.GONE) {
                int topMargin = -(mNoNetwork.getHeight() + RxBarTool.getStatusBarHeight(this));
                TranslateAnimation bottomToTop = new TranslateAnimation(0, 0, 0, topMargin);
                bottomToTop.setDuration(NO_NETWORK_ANIMATION_DURATION);
                mNoNetwork.startAnimation(bottomToTop);
                ViewTool.INSTANCE.setViewGone(mNoNetwork);
            }
            return;
        }

        ViewTool.INSTANCE.setViewVisible(mNoNetwork);
        Animation bottomToTop = AnimationUtils.loadAnimation(this, R.anim.anim_top_2_bottom);
        bottomToTop.setDuration(NO_NETWORK_ANIMATION_DURATION);
        mNoNetwork.startAnimation(bottomToTop);

        mNoNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxActivityTool.skipActivity(MainActivity.this, NetworkErrorActivity.class);
            }
        });
    }

    @Override
    public void onNetworkChangeListener(int status) {
        handleNoNetwork();
    }

>>>>>>> yangkeDevelopment
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
        RxSPTool.putString(this, KEY_DOWNLOAD_APK_URL, versionData.mUrl);
        if (RxDeviceTool.getAppVersionCode(this) < versionData.mVersion) {
            int ignoreVersion = RxSPTool.getInt(getApplicationContext(), KEY_VERSION_IGNORE);
            if (ignoreVersion == versionData.mVersion) {
                return;
            }
<<<<<<< HEAD
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(false)
                    .setTitle("我们更新了新版，快来体验！")
=======
            new AlertDialog.Builder(MainActivity.this)
                    .setCancelable(false)
                    .setTitle(R.string.app_new_version)
>>>>>>> yangkeDevelopment
                    .setMessage(vo.mMessage)
                    .setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
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
                                RxToast.warning(getString(R.string.sd_card_not_available));
                            }
                        }
<<<<<<< HEAD
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
=======
                    }).setNegativeButton(R.string.btn_cancle, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setNeutralButton(R.string.ignore_current_app_version, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RxSPTool.putInt(getApplicationContext(), KEY_VERSION_IGNORE, versionData.mVersion);
                            dialog.dismiss();
                        }
                    }).show();
>>>>>>> yangkeDevelopment
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        mSearchFragment = new SearchFragment();
        mVipFragment = new VipFragment();
        MeFragment meFragment = new MeFragment();
        adapter.addFragment(mSearchFragment);
        adapter.addFragment(mVipFragment);
        adapter.addFragment(meFragment);
        viewPager.setAdapter(adapter);
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
<<<<<<< HEAD
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        Intent it = new Intent(this, ApkDownloadService.class);
        stopService(it);
        super.onDestroy();
    }
=======
    };
>>>>>>> yangkeDevelopment

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
<<<<<<< HEAD
=======

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        WebView homeFragmentWebView = mVipFragment.getWebView();
        if (homeFragmentWebView != null && keyCode == KeyEvent.KEYCODE_BACK && homeFragmentWebView.canGoBack()) {
            homeFragmentWebView.goBack(); //webView goBack
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        RxToast.doubleClickExit(this);
    }

    @Override
    protected void onDestroy() {
        Intent it = new Intent(this, ApkDownloadService.class);
        stopService(it);
        super.onDestroy();
    }
>>>>>>> yangkeDevelopment
}
