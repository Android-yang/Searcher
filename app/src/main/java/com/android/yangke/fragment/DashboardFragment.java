package com.android.yangke.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.yangke.R;
import com.android.yangke.base.BaseLazyFragment;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends BaseLazyFragment {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersionBar.setTitleBar(getActivity(), mToolbar);

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_dashboard;
    }
}
