package com.android.yangke.activity;

import android.content.Intent;

import com.android.yangke.R;
import com.android.yangke.base.BaseActivity;
import com.android.yangke.base.BaseResponse;
import com.android.yangke.fragment.DashboardFragment;
import com.android.yangke.http.RequestListener;
import com.android.yangke.http.SearchTask;
import com.android.yangke.vo.MagnetVo;
import com.vondear.rxtools.view.RxToast;

import java.util.List;

/**
 * author: yangke on 5/30/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 磁力搜索结果
 */
public class SearchResultActivity extends BaseActivity implements RequestListener {

    protected SearchTask mSearchTask;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_search_result;
    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        String keyword = intent.getStringExtra(DashboardFragment.KEY_KEYWORD);//搜索关键字

        mSearchTask = new SearchTask();
        mSearchTask.setRequestListener(this, this);
        mSearchTask.execute(keyword);
    }

    @Override
    protected void initView() {
        setTileLeft(getString(R.string.search_result));
    }

    @Override
    public void onDataReceivedSuccess(List<? extends BaseResponse> list) {
        MagnetVo vo = (MagnetVo) list.get(0);
        RxToast.success(vo.mTitle);
    }

    @Override
    public void onDataReceiveFailed() {
        RxToast.warning("data is null");
    }

}
