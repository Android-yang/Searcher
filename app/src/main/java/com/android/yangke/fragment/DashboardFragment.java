package com.android.yangke.fragment;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
            final String par = mEtSearch.getText().toString().trim();
            if (TextUtils.isEmpty(par)) {
                RxToast.showToast("小哥哥，关键字不能为空！");
                return true;
            }

            if(isEroticism(par)){
                eroticismDialog(par);
            } else {
                action2SearchResultActivity(getActivity(), SearchResultActivity.class, par);
            }
            return true;
        }

        return false;
    }

    /**
     *
     * @param par 关键字
     * @return true 标志可能包含色情内容， false 反之
     */
    private boolean isEroticism(String par) {
        return par.contains("女") || par.contains("美女") || par.contains("加勒比")
                || par.contains("一本道") || par.contains("波多野结衣") || par.contains("舞")
                || par.contains("乳") || par.contains("巨") || par.contains("抹") || par.contains("爱");
    }

    private void eroticismDialog(final String par) {
        new AlertDialog.Builder(getContext())
                .setMessage("您搜索的内容可能包含成人内容！")
                .setCancelable(false)
                .setNegativeButton("未满18", null)
                .setPositiveButton("已满18", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        action2SearchResultActivity(getActivity(), SearchResultActivity.class, par);
                    }
                }).show();
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
        RxKeyboardUtils.hideSoftInput(act);

        Bundle bundle = new Bundle();
        bundle.putString(KEY_KEYWORD, pars);
        RxActivityUtils.skipActivity(act, cla, bundle);
    }
}
