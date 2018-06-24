package com.android.yangke.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxAppTool;
import com.vondear.rxtools.RxClipboardTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.vondear.rxtools.RxTool.getContext;

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
    public static final String KEY_USED_COUNT = "used_count";
    //可用次数（包含分享获取的次数）
    public static final String KEY_ALL_COUNT = "all_count";
    //免费次数
    public static final int FREE_COUNT = 80;//首次免费80
    private String mKeyword;
    private static final String KEY_TASK = "task";
    //网络加载比较慢，当用户直接从当前页面点击了返回键后，处理响应函数被回调， 但 View 已经被回收就会
    // 造成 NullPointerException，使用 mTaskList 存放任务，onDestory函数执行时，取消网络任务
    private ArrayList<SearchTask> mTaskList = new ArrayList();

    @Override
    protected int setLayoutId() {
        return R.layout.activity_search_result;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mKeyword = intent.getStringExtra(DashboardFragment.KEY_KEYWORD);//搜索关键字
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
        mTaskList.clear();
        SearchTask searchTask = new SearchTask();
        searchTask.setRequestListener(this);
        mTaskList.add(searchTask);
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
                if (!RxAppTool.appIsInstalled(SearchResultActivity.this, getString(R.string.thunder_package))) {
                    MeFragment.snakeBar(mRecyclerView, getString(R.string.hint_thunder_no_installed));
                    return;
                }
                if (isPay()) {
                    RxToast.error("免费次数已经用完");
                    //TODO 支付
//                    RxSPUtils.clearPreference(getApplicationContext(), KEY_USED_COUNT, KEY_USED_COUNT); //付费完成清空已使用次数
                    return;
                }

                RxClipboardTool.copyText(SearchResultActivity.this, vo.mMagnet);
                RxActivityTool.action2Thunder(getContext());
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
        int usedCountTemp = RxSPTool.getInt(getApplicationContext(), KEY_USED_COUNT);
        int usedCount = (usedCountTemp == -1 ? 0 : usedCountTemp);
        if (usedCount >= getFreeCount()) {
            return true;
        }
        ++usedCount;
        RxSPTool.putInt(getApplicationContext(), KEY_USED_COUNT, usedCount);
        RxSPTool.putInt(getApplicationContext(), KEY_ALL_COUNT, getFreeCount());
        return false;
    }

    /*
     * 剩余免费次数
     */
    private int getFreeCount() {
        int allCount = RxSPTool.getInt(getContext(), SearchResultActivity.KEY_ALL_COUNT);
        int freeCount = (-1 ==  allCount ? SearchResultActivity.FREE_COUNT : allCount);
        return freeCount;
    }

    private List mDataList = new ArrayList();

    @Override
    public void onDataReceivedSuccess(List list) {
        if(mRefreshLayout == null || mAdapter == null) {
            return;
        }
        if (mPage > 1) {
            if (list.size() == 0) {
                mAdapter.loadMoreEnd();
            } else {
                mDataList.addAll(list);
                mAdapter.loadMoreComplete();
            }
            return;
        }
        if (mPage == 1) {
            mDataList.clear();
            mDataList.addAll(list);
            mRefreshLayout.finishRefreshing();
            if (mDataList.size() == 0) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTaskList.size() > 0) {
            SearchTask delayTask = mTaskList.get(0);
            if (delayTask != null && delayTask.getStatus() == AsyncTask.Status.RUNNING) {
                delayTask.cancel(true);
                delayTask = null;
            }
        }
    }
}
