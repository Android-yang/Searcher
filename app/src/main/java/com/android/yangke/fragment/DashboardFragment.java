package com.android.yangke.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.android.yangke.R;
import com.android.yangke.activity.SearchResultActivity;
import com.android.yangke.base.BaseLazyFragment;
import com.gyf.barlibrary.ImmersionBar;
import com.vondear.rxtools.RxActivityUtils;
import com.vondear.rxtools.RxKeyboardUtils;
import com.vondear.rxtools.view.RxToast;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends BaseLazyFragment implements View.OnKeyListener {

    public static final String KEY_KEYWORD = "keyword";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.dashboard_et_title)
    EditText mEtSearch;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersionBar.setTitleBar(getActivity(), mToolbar);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_dashboard;
    }

    @Override
    protected void initData() {
        mEtSearch.setOnKeyListener(this);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (isSearch(v, keyCode, event)) {
            String par = mEtSearch.getText().toString().trim();
            if (TextUtils.isEmpty(par)) {
                RxToast.showToast("小哥哥，关键字不能为空！");
                return true;
            }
            RxKeyboardUtils.hideSoftInput(getActivity());
            action2SearchResultActivity(getActivity(), SearchResultActivity.class, par);
            return true;
        }

        return false;
    }

    /**
     * @param v
     * @param keyCode
     * @param event
     * @return true 标志用户点击了回车（搜索）false反之
     */
    private boolean isSearch(View v, int keyCode, KeyEvent event) {
        return v.getId() == R.id.dashboard_et_title && event.getAction() == KeyEvent.ACTION_DOWN
                && keyCode == KeyEvent.KEYCODE_ENTER;
    }

    public static void action2SearchResultActivity(Activity act, Class cla, String pars) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_KEYWORD, pars);
        RxActivityUtils.skipActivity(act, cla, bundle);
    }
}
