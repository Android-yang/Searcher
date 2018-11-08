package com.android.yangke.fragment;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.yangke.R;
import com.android.yangke.activity.SearchResultActivity;
import com.android.yangke.adapter.AbsBaseAdapter;
import com.android.yangke.base.BaseApplication;
import com.android.yangke.base.BaseLazyFragment;
import com.android.yangke.tool.Constant;
import com.android.yangke.tool.ViewTool;
import com.android.yangke.vo.DaoSession;
import com.android.yangke.vo.SearchHistoryBeen;
import com.android.yangke.vo.SearchHistoryBeenDao;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
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
public class SearchFragment extends BaseLazyFragment implements View.OnKeyListener {

    public static final String KEY_KEYWORD = "keyword";

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.search_et_title) EditText mEtSearch;
    @BindView(R.id.search_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.search_tv_clear) TextView mTxtClearHistory;
    @BindView(R.id.search_recycler_view_history) RecyclerView mHistoryRecyclerView;
    @BindView(R.id.nested_scrollView) NestedScrollView mNestedScrollView;

    private ArrayList<String> mDataList;
    private SearchHistoryBeenDao mSearchHistoryDao;
    //最近搜过
    private ArrayList<String> mHistoryDataList = new ArrayList<>();
    private HistorySearchAdapter mSearchHistoryAdapter;
    private HotSearchAdapter mAdapter;

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
    protected int setLayoutId() {return R.layout.fragment_search;}

    @Override
    protected void initData() {
        super.initData();
        mEtSearch.setOnKeyListener(this);
        iniHotSearchDataList();
        iniBeenDao();
        iniSearchHistory();
        iniHotSearch();
    }

    private void iniHotSearch() {
        mAdapter = new HotSearchAdapter(R.layout.item_one, mDataList);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext());
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        mRecyclerView.setLayoutManager(flexboxLayoutManager);
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
        mHistoryDataList.clear();
        for (SearchHistoryBeen s : searchHistory) {
            mHistoryDataList.add(s.getKeyword());
        }
        mSearchHistoryAdapter = new HistorySearchAdapter(R.layout.item_dashboard_history, mHistoryDataList);
        mHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSearchHistoryAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mHistoryRecyclerView.setAdapter(mSearchHistoryAdapter);
        if(mHistoryDataList.size() > 0) {
            ViewTool.INSTANCE.setViewVisible(mTxtClearHistory);
        } else {
            ViewTool.INSTANCE.setViewGone(mTxtClearHistory);
        }
        mSearchHistoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String currentClickItem = (String) adapter.getData().get(position);
                saveSearchHistory2DB(currentClickItem);
                action2SearchResultActivity(getActivity(), SearchResultActivity.class, currentClickItem);
            }
        });

        //解决当屏幕内内容大于一屏时出现默认滚动到底部的问题
        mNestedScrollView.post(new Runnable() {
            @Override public void run() { mNestedScrollView.fullScroll(ScrollView.FOCUS_UP); }
        });
    }

    private List<SearchHistoryBeen> querySearchHistoryDataList() {
        return mSearchHistoryDao.queryBuilder()
                .where(SearchHistoryBeenDao.Properties.Time.notEq(0))
                .orderDesc(SearchHistoryBeenDao.Properties.Time)
                .build()
                .list();
    }

    @OnClick(R.id.search_tv_clear)
    public void handleClick() {
        mSearchHistoryDao.deleteAll();
        mHistoryDataList.clear();
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_right_2_left);
        mTxtClearHistory.startAnimation(animation);
        mSearchHistoryAdapter.setNewData(null);
        ViewTool.INSTANCE.setViewGone(mTxtClearHistory);
        BaseApplication.instance().mMainHandler.postDelayed(new Runnable() {
            @Override public void run() {
                RxAnimationTool.animateHeight(0, mRecyclerView.getHeight(), mRecyclerView);
            }
        }, animation.getDuration());
    }

    /**
     * 热门搜索数据
     */
    private void iniHotSearchDataList() {
        mDataList = new ArrayList<>();
        mDataList.add("王宝强");
        mDataList.add("延禧攻略");
        mDataList.add("甄子丹");
        mDataList.add("摩天营救");
        mDataList.add("羞羞的铁拳");
        mDataList.add("加勒比海盗");
        mDataList.add("复仇者联盟");
        mDataList.add("西虹市首富");
        mDataList.add("史泰龙");
        mDataList.add("李连杰");
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (isSearch(v, keyCode, event)) {
            final String par = mEtSearch.getText().toString().trim();
            if (TextUtils.isEmpty(par)) {
                RxToast.showToast(getString(R.string.toast_keyword_non_null));
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
        return par.contains(getString(R.string.audult_1)) || par.contains(getString(R.string.audult_2))
                || par.contains(getString(R.string.audult_3)) || par.contains(getString(R.string.audult_4))
                || par.contains(getString(R.string.audult_6)) || par.contains(getString(R.string.audult_5));
    }

    private void eroticismDialog(final String par) {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.hint_adult_content)
                .setCancelable(false)
                .setNegativeButton(R.string.under_18, null)
                .setPositiveButton(R.string.old_18, new DialogInterface.OnClickListener() {
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
        return v.getId() == R.id.search_et_title && event.getAction() == KeyEvent.ACTION_DOWN
                && keyCode == KeyEvent.KEYCODE_ENTER;
    }

    /**
     * 热门搜索
     */
    private class HotSearchAdapter extends AbsBaseAdapter<String, BaseViewHolder> {

        public HotSearchAdapter(int layoutResId, @Nullable List<String> data) { super(layoutResId, data); }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            int[] colors = Constant.INSTANCE.getCOLORS();
            int color = colors[helper.getLayoutPosition() % colors.length];
            helper.setBackgroundColor(R.id.txt_hot_search, color)
                    .setText(R.id.txt_hot_search, item);
        }
    }

    /**
     * 最近搜过
     */
    private class HistorySearchAdapter extends AbsBaseAdapter<String, BaseViewHolder> {

        public HistorySearchAdapter(int layoutResId, @Nullable List data) { super(layoutResId, data); }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.text, item);
        }
    }
}
