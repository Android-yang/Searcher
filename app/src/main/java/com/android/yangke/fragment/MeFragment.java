package com.android.yangke.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.yangke.R;
import com.android.yangke.activity.AboutAuthorActivity;
import com.android.yangke.activity.AuthorActivity;
import com.android.yangke.activity.SearchResultActivity;
import com.android.yangke.activity.SoftwareRequiredActivity;
import com.android.yangke.base.BaseApplication;
import com.android.yangke.base.BaseLazyFragment;
import com.android.yangke.tool.Constant;
import com.android.yangke.tool.ViewTool;
import com.android.yangke.view.TapTargetView.TapTarget;
import com.android.yangke.view.TapTargetView.TapTargetSequence;
import com.android.yangke.wxapi.WXEntryActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxAppTool;
import com.vondear.rxtools.RxClipboardTool;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.RxPayTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxToast;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author: yangke on 2018/5/20
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 我
 */
public class MeFragment extends BaseLazyFragment {

    @BindView(R.id.me_tv_title) TextView mTitle;
    @BindView(R.id.mt_tv_versionCode) TextView mVersionCode;
    @BindView(R.id.me_ll_personal_msg) LinearLayout mLLPersonalMsg;
    @BindView(R.id.me_ll_youhui) LinearLayout mLLYouHui;
    @BindView(R.id.me_ll_fapiao) LinearLayout mLLFaPiao;
    @BindView(R.id.me_tv_tuijian) TextView mTvTuiJian;
    @BindView(R.id.me_tv_account) TextView mTvAccount;
    @BindView(R.id.me_tv_qq_flock) TextView mTvMsg;
    @BindView(R.id.me_tv_mianze) TextView mTvMianZe;
    @BindView(R.id.me_txt_free) TextView mFree;//剩余次数
    @BindView(R.id.me_txt_author) TextView mTvAuthorMsg;

    private WebView mWebView;//增加访问量

    private String[] urlAry = {
            "https://www.jianshu.com/p/bca0f815f271", "https://www.jianshu.com/p/4c19ddd9f02f"
            , "https://www.jianshu.com/p/97a7ead430d4", "https://www.jianshu.com/p/fb31c275b3e7"
            , "https://www.jianshu.com/p/886ab64c1afa", "https://www.jianshu.com/p/5cb0901050ad"
            , "https://www.jianshu.com/p/e9fd1b7fb30f", "https://www.jianshu.com/p/e9fd1b7fb30f"
            , "https://www.jianshu.com/p/0fb2a78208aa", "https://www.jianshu.com/p/2f1964f98e44"
    };

    public static void snakeBar(View v, String hint) {
        Snackbar.make(v, hint, Snackbar.LENGTH_SHORT)
                .setAction("Action", null)
                .show();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersionBar.setTitleBar(getActivity(), mTitle);
    }

    @Override
    protected void initData() { }

    /*
     * 剩余免费次数
     */
    private int getFreeCount() {
        int allCount = RxSPTool.getInt(getContext(), RxPayTool.KEY_ALL_COUNT);
        int usedCount = RxSPTool.getInt(getContext(), RxPayTool.KEY_USED_COUNT);
        int showCount = (allCount == -1 ? RxPayTool.FREE_COUNT : allCount) - (usedCount == -1 ? 0 : usedCount);
        return showCount;
    }

    @OnClick({R.id.me_tv_guanyu, R.id.me_tv_qq, R.id.me_ll_personal_msg, R.id.me_ll_free, R.id.me_ll_youhui,
            R.id.me_ll_fapiao, R.id.me_tv_tuijian, R.id.me_tv_account, R.id.me_tv_qq_flock, R.id.me_tv_mianze})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.me_ll_personal_msg://作者个人中心
                RxActivityTool.skipActivity(getActivity(), AuthorActivity.class);
                break;
            case R.id.me_tv_guanyu://关于作者
                RxActivityTool.skipActivity(getActivity(), AboutAuthorActivity.class);
                break;
            case R.id.me_tv_qq://作者QQ
                RxClipboardTool.copyText(getContext(), VipFragment.Companion.getQQ());
                snakeBar(v, getString(R.string.toast_copy_author_qq_success));
                break;
            case R.id.me_ll_youhui://
                break;
            case R.id.me_ll_fapiao:
                doubleVisit();//增加访问量
                int visit = RxSPTool.getInt(getContext(), Constant.INSTANCE.getKEY_VISIT());
                if(visit > 57) {//双击 60 次开启vip功能
                    RxSPTool.putBoolean(getContext(), Constant.INSTANCE.getKEY_VIP(), true);
                    RxToast.warning(getString(R.string.toast_is_vip_restart));
                } else {
                    visit++;
                    RxSPTool.putInt(getContext(), Constant.INSTANCE.getKEY_VISIT(), visit);
                }
                break;
            case R.id.me_ll_free://剩余次数
                int freeCount = getFreeCount();
                String hintMsg = getString(R.string.toast_use_count_) + freeCount + getString(R.string._toast_use_count);
                if (freeCount == 0) {
                    snakeBar(v, hintMsg);
                } else {
                    RxToast.warning(hintMsg);
                }
                break;
            case R.id.me_tv_tuijian://推荐有奖
                RxActivityTool.skipActivity(getActivity(), WXEntryActivity.class);
                break;
            case R.id.me_tv_account://账户安全
                RxToast.warning(getString(R.string.toast_account_self));
                break;
            case R.id.me_tv_qq_flock://QQ 群
                snakeBar(v, getString(R.string.copy_author_qq_flock_success));
                RxClipboardTool.copyText(getContext(), VipFragment.Companion.getQQ_FLOCK());
                break;
            case R.id.me_tv_mianze://免责条款
                RxActivityTool.skipActivity(getActivity(), SoftwareRequiredActivity.class);
                break;
        }
    }

    private void doubleVisit() {
        String url = randomUrl();
        RxLogTool.d("url------------>"+url);
        mWebView.loadUrl(url);
    }

    private String randomUrl() {
        Random random = new Random();
        int index = random.nextInt(urlAry.length);
        return urlAry[index];
    }

    @Override
    protected void initView() {
        mWebView = new WebView(getContext());
        mVersionCode.setText(getString(R.string.app_version) + RxAppTool.getAppVersionName(getContext()));
        ViewTool.INSTANCE.textSetTypeface(mTvAuthorMsg, BaseApplication.instance(), Constant.INSTANCE.getFONT_FOUNDER_SIMPLIFIED());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onVisible() {
        mFree.setText(getFreeCount() + "次");
        if (RxSPTool.isFirstOpenApp(BaseApplication.instance(), Constant.FIRST_OPEN_INDICATE)) {
            showTapTarget();
        }
    }

    /**
     * 引导用户使用
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showTapTarget() {
        TapTargetSequence sequence = new TapTargetSequence(getActivity())
                .targets(TapTarget.forView(mTvMsg, getString(R.string.join_flock_welfare))
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.c_ff541f)
                                .drawShadow(true)
                                .cancelable(false)
                                .id(1),
                        TapTarget.forView(mFree, getString(R.string.surplus_free_time)).dimColor(android.R.color.black)
                                .outerCircleColor(R.color.c_ff541f)
                                .drawShadow(true)
                                .id(2)
                                .cancelable(false)
//                        TapTarget.forBounds(target, "点击这里切换新闻", "双击返回顶部\n再次双击刷新当前页面")
//                                .dimColor(android.R.color.black)
//                                .outerCircleColor(R.color.c_ff541f)
//                                .transparentTarget(true)
//                                .drawShadow(true)
//                                .cancelable(false)
//                                .id(3)
                ).listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() { setFirstOpenAppToFalse(); }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) { }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) { setFirstOpenAppToFalse(); }

                    private void setFirstOpenAppToFalse() {
                        RxSPTool.putBoolean(BaseApplication.instance(), Constant.FIRST_OPEN_INDICATE, false);
                    }
                });
        sequence.start();
    }
}
