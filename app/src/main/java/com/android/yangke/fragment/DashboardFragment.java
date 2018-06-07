package com.android.yangke.fragment;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.yangke.R;
import com.android.yangke.activity.SearchResultActivity;
import com.android.yangke.base.BaseApplication;
import com.android.yangke.base.BaseLazyFragment;
import com.android.yangke.vo.DaoSession;
import com.android.yangke.vo.SearchHistoryBeen;
import com.android.yangke.vo.SearchHistoryBeenDao;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gyf.barlibrary.ImmersionBar;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxAnimationTool;
import com.vondear.rxtools.RxKeyboardTool;
import com.vondear.rxtools.view.RxToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author: yangke on 6/5/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 主页
 * greenDao 使用可参考：https://blog.csdn.net/u012702547/article/details/52226163
 */
public class DashboardFragment extends BaseLazyFragment implements View.OnKeyListener {

    public static final String KEY_KEYWORD = "keyword";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.dashboard_et_title)
    EditText mEtSearch;
    @BindView(R.id.dashboard_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.dashboard_recycler_view_history)
    RecyclerView mHistoryRecyclerView;
    @BindView(R.id.dashboard_ll_history)
    LinearLayout mLLHistory;

    private HotSearchAdapter mAdapter;

    private ArrayList<String> mDataList;
    private SearchHistoryBeenDao mSearchHistoryDao;
    //最近搜过
    private ArrayList<String> mHistoryDataList = new ArrayList<>();
    ;

    public static void action2SearchResultActivity(Activity act, Class cla, String pars) {
        RxKeyboardTool.hideSoftInput(act);

        Bundle bundle = new Bundle();
        bundle.putString(KEY_KEYWORD, pars);
        RxActivityTool.skipActivity(act, cla, bundle);
    }

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
        super.initData();
        mEtSearch.setOnKeyListener(this);
        iniHotSearchDataList();
        iniBeenDao();
        iniSearchHistory();

        mAdapter = new HotSearchAdapter(R.layout.item_one, mDataList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String currentClickItem = (String) adapter.getData().get(position);
                saveSearchHistory2DB(currentClickItem);
                action2SearchResultActivity(getActivity(), SearchResultActivity.class, currentClickItem);
            }
        });
    }

    private void iniBeenDao() {
        DaoSession daoSession = BaseApplication.instance().getDaoSession();
        mSearchHistoryDao = daoSession.getSearchHistoryBeenDao();
    }

    @Override
    public void onResume() {
        super.onResume();
        iniSearchHistory();
    }

    /**
     * 最近搜过
     */
    private void iniSearchHistory() {
        if (mSearchHistoryDao == null) {
            return;
        }
        List<SearchHistoryBeen> searchHistory = querySearchHistoryDataList();
        if (searchHistory.size() > 0) {
            mHistoryDataList.clear();
            for (SearchHistoryBeen s : searchHistory) {
                mHistoryDataList.add(s.getKeyword());
            }
            mLLHistory.setVisibility(View.VISIBLE);
            HistorySearchAdapter searchHistoryAdapter = new HistorySearchAdapter(R.layout.item_dashboard_history, mHistoryDataList);
            mHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            searchHistoryAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
            mHistoryRecyclerView.setAdapter(searchHistoryAdapter);

            searchHistoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    String currentClickItem = (String) adapter.getData().get(position);
                    saveSearchHistory2DB(currentClickItem);
                    action2SearchResultActivity(getActivity(), SearchResultActivity.class, currentClickItem);
                }
            });
        }
    }

    private List<SearchHistoryBeen> querySearchHistoryDataList() {
        return mSearchHistoryDao.queryBuilder()
                .where(SearchHistoryBeenDao.Properties.Time.notEq(0))
                .orderDesc(SearchHistoryBeenDao.Properties.Time)
                .build()
                .list();
    }

    @OnClick(R.id.dashboard_tv_clear)
    public void handleClick() {
        mSearchHistoryDao.deleteAll();
        mHistoryDataList.clear();
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_right_2_left);
        mLLHistory.startAnimation(animation);
        BaseApplication.instance().mMainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLLHistory.setVisibility(View.GONE);
                RxAnimationTool.animateHeight(0, mRecyclerView.getHeight(), mRecyclerView);
            }
        }, animation.getDuration());
    }

    /**
     * 热门搜索数据
     */
    private void iniHotSearchDataList() {
        mDataList = new ArrayList<>();
        mDataList.add("加勒比海盗");
        mDataList.add("唐人街探案");
        mDataList.add("捉妖记");
        mDataList.add("刘德华");
        mDataList.add("复仇者联盟");
        mDataList.add("成龙");
        mDataList.add("变形金刚");
        mDataList.add("羞羞的铁拳");
        mDataList.add("甄子丹");
        mDataList.add("徐峥");
        mDataList.add("熊出没");
        mDataList.add("周星驰");
        mDataList.add("王宝强");
        mDataList.add("黄渤");
        mDataList.add("史泰龙");
        mDataList.add("李连杰");
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (isSearch(v, keyCode, event)) {
            final String par = mEtSearch.getText().toString().trim();
            if (TextUtils.isEmpty(par)) {
                RxToast.showToast("小哥哥，关键字不能为空！");
                return true;
            }

            if (isEroticism(par)) {
                eroticismDialog(par);
            } else {
                action2SearchResultActivity(getActivity(), SearchResultActivity.class, par);
            }
            saveSearchHistory2DB(par);
            return true;
        }
        return false;
    }

    /**
     * 保存搜索记录
     *
     * @param keyword 关键子
     */
    private void saveSearchHistory2DB(String keyword) {
        if (mHistoryDataList != null) {
            if (!isSaveHistory(keyword)) {
                SearchHistoryBeen searchHistoryBeen = new SearchHistoryBeen(null, keyword, System.currentTimeMillis());
                mSearchHistoryDao.insert(searchHistoryBeen);
            }
        }
    }

    /**
     * @param keyword 关键字
     * @return true DB中已经保存过此关键字，false反之
     */
    private boolean isSaveHistory(String keyword) {
        if (mHistoryDataList.size() == 0) {
            return false;
        }
        for (String key : mHistoryDataList) {
            if (keyword.equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
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

    /**
     * 热门搜索
     */
    private class HotSearchAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public HotSearchAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.text, item);
        }
    }

    /**
     * 最近搜过
     */
    private class HistorySearchAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public HistorySearchAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.text, item);
        }
    }
}
