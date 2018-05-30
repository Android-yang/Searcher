package com.android.yangke.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.yangke.R;
import com.android.yangke.adapter.MagnetAdapter;
import com.android.yangke.base.BaseActivity;
import com.android.yangke.base.BaseResponse;
import com.android.yangke.fragment.DashboardFragment;
import com.android.yangke.http.RequestListener;
import com.android.yangke.http.SearchTask;
import com.android.yangke.vo.MagnetVo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.vondear.rxtools.view.RxToast;

import java.util.List;

import butterknife.BindView;

/**
 * author: yangke on 5/30/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 磁力搜索结果
 */
public class SearchResultActivity extends BaseActivity implements RequestListener {

    protected SearchTask mSearchTask;

    @BindView(R.id.dashboard_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.dashboard_refreshLayout)
    TwinklingRefreshLayout mRefreshLayout;
    private MagnetAdapter mAdapter;

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
//        mSearchTask.execute(keyword);
        mSearchTask.execute("加勒比");
    }

    @Override
    protected void initView() {
        setTileLeft(getString(R.string.search_result));

        LinearLayoutManager llManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(llManager);
        mAdapter = new MagnetAdapter(R.layout.item_search_magnet_result, null);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDataReceivedSuccess(List list) {
        mAdapter.setNewData(list);
    }

    @Override
    public void onDataReceiveFailed() {
        RxToast.warning("data is null");
    }

}
