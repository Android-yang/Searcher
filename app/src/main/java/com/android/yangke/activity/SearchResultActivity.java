package com.android.yangke.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.android.yangke.R;
import com.android.yangke.adapter.MagnetAdapter;
import com.android.yangke.base.BaseActivity;
import com.android.yangke.fragment.DashboardFragment;
import com.android.yangke.fragment.MeFragment;
import com.android.yangke.http.RequestListener;
import com.android.yangke.http.SearchTask;
import com.android.yangke.util.AppTools;
import com.android.yangke.vo.MagnetVo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.vondear.rxtools.RxClipboardUtils;
import com.vondear.rxtools.RxLogUtils;
import com.vondear.rxtools.RxSPUtils;
import com.vondear.rxtools.view.RxToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author: yangke on 5/30/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 磁力搜索结果
 */
public class SearchResultActivity extends BaseActivity implements RequestListener {

    @BindView(R.id.dashboard_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.dashboard_refreshLayout)
    TwinklingRefreshLayout mRefreshLayout;
    private MagnetAdapter mAdapter;
    private int mPage = 1;//请求页数(例：第一页，第二页)

    //使用次数
    private static final String KEY_USED_COUNT = "used_count";
    //免费次数
    private static final int FREE_COUNT = 80;
    private String mKeyword;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_search_result;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mKeyword = intent.getStringExtra(DashboardFragment.KEY_KEYWORD);//搜索关键字
        iniSearchTask();
        executeTask(iniSearchTask(), mKeyword, mPage);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MagnetAdapter(R.layout.item_search_magnet_result, mDataList);
        mAdapter.openLoadAnimation(BaseQuickAdapter.FOOTER_VIEW);
        mRecyclerView.setAdapter(mAdapter);
        //隐藏 refreshLayout 的加载更多
        mRefreshLayout.setEnableLoadmore(false);
        //首次加载展示加载条
        mRefreshLayout.startRefresh();
    }

    private SearchTask iniSearchTask() {
        SearchTask searchTask = new SearchTask();
        searchTask.setRequestListener(this);
        return searchTask;
    }

    @Override
    protected void initView() {
        setTileLeft(getString(R.string.search_result));
    }

    @Override
    protected void setListener() {
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPage++;
                executeTask(iniSearchTask(), mKeyword, mPage);
            }
        }, mRecyclerView);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MagnetVo vo = (MagnetVo) adapter.getItem(position);
                if (!AppTools.appIsInstalled(SearchResultActivity.this, getString(R.string.thunder_package))) {
                    MeFragment.snakeBar(mRecyclerView, getString(R.string.hint_thunder_no_installed));
                    return;
                }
                if (isPay()) {
                    RxToast.warning("免费次数已经用完");
                    //TODO 支付
//                    RxSPUtils.clearPreference(getApplicationContext(), KEY_USED_COUNT, KEY_USED_COUNT); //付费完成清空已使用次数
                    return;
                }

                RxClipboardUtils.copyText(SearchResultActivity.this, vo.mMagnet);
                action2Thunder();
            }
        });

        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPage = 1;
                executeTask(iniSearchTask(), mKeyword, mPage);
            }
        });
    }

    private void executeTask(SearchTask task, String keyword, int page) {
        task.execute(keyword, String.valueOf(page));
    }

    /**
     * 是否需要支付
     *
     * @return
     */
    private boolean isPay() {
        int usedCount = RxSPUtils.getInt(getApplicationContext(), KEY_USED_COUNT);
        usedCount++;
        if (usedCount >= FREE_COUNT) {
            return true;
        }
        RxSPUtils.putInt(getApplicationContext(), KEY_USED_COUNT, usedCount);
        return false;
    }

    private void action2Thunder() {
        Intent intent = new Intent();
        ComponentName cmp = new ComponentName(getString(R.string.thunder_package), "com.xunlei.downloadprovider.launch.LaunchActivity");
        intent.setComponent(cmp);
        startActivity(intent);
    }

    private List mDataList = new ArrayList();
    @Override
    public void onDataReceivedSuccess(List list) {
        if(mPage > 1) {
            if(list.size() == 0) {
                mAdapter.loadMoreEnd();
            } else {
                mDataList.addAll(list);
                mAdapter.loadMoreComplete();
            }
            return;
        }
        if(mPage == 1) {
            mDataList.clear();
            mDataList.addAll(list);
            mRefreshLayout.finishRefreshing();
            if(mDataList.size() == 0) {
                View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view_base, null);
                mAdapter.setEmptyView(emptyView);
                return;
            }
            //当下拉到无更多数据时，DataSetChanged 函数不能清除已有状态，需调用 loadMoreComplete
            mAdapter.loadMoreComplete();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDataReceiveFailed() {
        RxToast.warning(getString(R.string.hint_no_data));
    }

}
