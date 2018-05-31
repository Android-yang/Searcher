package com.android.yangke.fragment;


import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.yangke.R;
import com.android.yangke.activity.AboutAuthorActivity;
import com.android.yangke.activity.SoftwareRequiredActivity;
import com.android.yangke.base.BaseLazyFragment;
import com.gyf.barlibrary.ImmersionBar;
import com.vondear.rxtools.RxActivityUtils;
import com.vondear.rxtools.RxAppUtils;
import com.vondear.rxtools.RxClipboardUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author: yangke on 2018/5/20
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 我
 */
public class MeFragment extends BaseLazyFragment {

    @BindView(R.id.me_tv_title)
    TextView mTitle;
    @BindView(R.id.mt_tv_versionCode)
    TextView mVersionCode;
    @BindView(R.id.me_ll_personal_msg)
    LinearLayout mLLPersonalMsg;
    @BindView(R.id.me_ll_yuer)
    LinearLayout mLLYuEr;
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

    private static final String HINT_QQ = "您已成功复制作者QQ";
    private static final String HINT_QQ_FLOCK = "您已成功复制作者QQ群";

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersionBar.setTitleBar(getActivity(), mTitle);
    }

    @OnClick({R.id.me_tv_guanyu, R.id.me_tv_qq, R.id.me_ll_personal_msg, R.id.me_ll_yuer, R.id.me_ll_youhui,
            R.id.me_tv_tuijian, R.id.me_tv_account, R.id.me_tv_qq_flock, R.id.me_tv_mianze})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.me_ll_personal_msg:
                RxClipboardUtils.copyText(getContext(), HomeFragment.QQ_FLOCK);
                snakeBar(v, HINT_QQ_FLOCK);
                break;
            case R.id.me_tv_guanyu:
                RxActivityUtils.skipActivity(getActivity(), AboutAuthorActivity.class);
                break;
            case R.id.me_tv_qq:
                RxClipboardUtils.copyText(getContext(), HomeFragment.QQ);
                snakeBar(v, HINT_QQ);
                break;
            case R.id.me_ll_yuer:
                snakeBar(v, HINT_QQ);
                break;
            case R.id.me_ll_youhui:
                snakeBar(v, HINT_QQ);
                break;
            case R.id.me_tv_tuijian:
                snakeBar(v, HINT_QQ);
                break;
            case R.id.me_tv_account:
                snakeBar(v, HINT_QQ);
                break;
            case R.id.me_tv_qq_flock:
                snakeBar(v, HINT_QQ_FLOCK);
                RxClipboardUtils.copyText(getContext(), HomeFragment.QQ_FLOCK);
                break;
            case R.id.me_tv_mianze:
                RxActivityUtils.skipActivity(getActivity(), SoftwareRequiredActivity.class);
                break;
        }
    }

    public static void snakeBar(View v, String hint) {
        Snackbar.make(v, hint, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    @Override
    protected void initView() {
        super.initView();

        mVersionCode.setText("版本V" + RxAppUtils.getAppVersionName(getContext()));
    }
}
