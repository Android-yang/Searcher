package com.android.yangke.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.yangke.R;
import com.android.yangke.base.BaseActivity;
import com.android.yangke.fragment.DashboardFragment;
import com.android.yangke.fragment.HomeFragment;
import com.android.yangke.fragment.MeFragment;
import com.android.yangke.listener.OnPageChangeListener_;
import com.android.yangke.view.ViewPagerNoScroller;
import com.vondear.rxtools.view.RxToast;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        mViewPager = (ViewPagerNoScroller) findViewById(R.id.viewpager);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
}
