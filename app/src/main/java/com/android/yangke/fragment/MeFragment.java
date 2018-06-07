package com.android.yangke.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.yangke.R;
import com.android.yangke.activity.AboutAuthorActivity;
import com.android.yangke.activity.SearchResultActivity;
import com.android.yangke.activity.SoftwareRequiredActivity;
import com.android.yangke.base.BaseLazyFragment;
import com.android.yangke.wxapi.WXEntryActivity;
import com.gyf.barlibrary.ImmersionBar;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxAppTool;
import com.vondear.rxtools.RxClipboardTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxToast;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author: yangke on 2018/5/20
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 我
 */
public class MeFragment extends BaseLazyFragment {

    private static final String HINT_QQ = "您已成功复制作者QQ";
    private static final String HINT_QQ_FLOCK = "您已成功复制作者QQ群";
    @BindView(R.id.me_tv_title)
    TextView mTitle;
    @BindView(R.id.mt_tv_versionCode)
    TextView mVersionCode;
    @BindView(R.id.me_ll_personal_msg)
    LinearLayout mLLPersonalMsg;
    @BindView(R.id.me_ll_youhui)
    LinearLayout mLLYouHui;
    @BindView(R.id.me_ll_fapiao)
    LinearLayout mLLFaPiao;
    @BindView(R.id.me_tv_tuijian)
    TextView mTvTuiJian;
    @BindView(R.id.me_tv_account)
    TextView mTvAccount;
    @BindView(R.id.me_tv_qq_flock)
    TextView mTvMsg;
    @BindView(R.id.me_tv_mianze)
    TextView mTvMianZe;
    @BindView(R.id.me_txt_free)
    TextView mFree;//剩余次数

    public static void snakeBar(View v, String hint) {
        Snackbar.make(v, hint, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
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
    public void onResume() {
        super.onResume();
        mFree.setText(getFreeCount() + "次");
    }

    /*
     * 剩余免费次数
     */
    private int getFreeCount() {
        int allCount = RxSPTool.getInt(getContext(), SearchResultActivity.KEY_ALL_COUNT);
        int usedCount = RxSPTool.getInt(getContext(), SearchResultActivity.KEY_USED_COUNT);
        int showCount = (allCount == -1 ? SearchResultActivity.FREE_COUNT : allCount) - (usedCount == -1 ? 0 : usedCount);
        return showCount;
    }

    @OnClick({R.id.me_tv_guanyu, R.id.me_tv_qq, R.id.me_ll_personal_msg, R.id.me_ll_free, R.id.me_ll_youhui,
            R.id.me_tv_tuijian, R.id.me_tv_account, R.id.me_tv_qq_flock, R.id.me_tv_mianze})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.me_ll_personal_msg:
                RxClipboardTool.copyText(getContext(), HomeFragment.QQ);
                snakeBar(v, HINT_QQ);
                break;
            case R.id.me_tv_guanyu:
                RxActivityTool.skipActivity(getActivity(), AboutAuthorActivity.class);
                break;
            case R.id.me_tv_qq:
                RxClipboardTool.copyText(getContext(), HomeFragment.QQ);
                snakeBar(v, HINT_QQ);
                break;
            case R.id.me_ll_youhui:

                break;
            case R.id.me_ll_free:
                int freeCount = getFreeCount();
                String hintMsg = "可使用剩余次数" + freeCount + "次， 你可通过分享此软件获取体验次数！";
                if (freeCount == 0) {
                    snakeBar(v, hintMsg);
                } else {
                    RxToast.warning(hintMsg);
                }
                break;
            case R.id.me_tv_tuijian:
                RxActivityTool.skipActivity(getActivity(), WXEntryActivity.class);
                break;
            case R.id.me_tv_account:
                RxToast.warning("已经很安全了");
                break;
            case R.id.me_tv_qq_flock:
                snakeBar(v, HINT_QQ_FLOCK);
                RxClipboardTool.copyText(getContext(), HomeFragment.QQ_FLOCK);
                break;
            case R.id.me_tv_mianze:
                RxActivityTool.skipActivity(getActivity(), SoftwareRequiredActivity.class);
                break;
        }
    }

    @Override
    protected void initView() {
        super.initView();
        mVersionCode.setText("版本V" + RxAppTool.getAppVersionName(getContext()));
    }
}
