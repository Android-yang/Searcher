package com.android.yangke.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
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
import com.android.yangke.fragment.HomeFragment;
import com.android.yangke.fragment.MeFragment;
import com.android.yangke.fragment.SearchFragment;
import com.android.yangke.http.BaseParam;
import com.android.yangke.http.NetworkTask;
import com.android.yangke.http.Request;
import com.android.yangke.http.ResponseCode;
import com.android.yangke.http.ServiceMap;
import com.android.yangke.listener.OnPageChangeListener_;
import com.android.yangke.service.ApkDownloadService;
import com.android.yangke.tool.Constant;
import com.android.yangke.tool.ViewTool;
import com.android.yangke.view.TapTargetView.TapTarget;
import com.android.yangke.view.TapTargetView.TapTargetSequence;
import com.android.yangke.view.ViewPagerNoScroller;
import com.android.yangke.vo.AppVersionVo;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxBarTool;
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

    private BottomNavigationView mBottomNavigationView;
    private ViewPagerNoScroller mViewPager;
    private HomeFragment mHomeFragment;
    private View mNoNetwork;

    //忽略版本升级
    private static final String KEY_VERSION_IGNORE = "version_ignore";
    private static final String APK_NAME = "search.apk";
    private static final int NO_NETWORK_ANIMATION_DURATION = 700;
    private SearchFragment mSearchFragment;


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
            new AlertDialog.Builder(MainActivity.this)
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
        mHomeFragment = new HomeFragment();
        MeFragment meFragment = new MeFragment();
        adapter.addFragment(mSearchFragment);
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
}
