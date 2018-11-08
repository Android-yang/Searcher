package com.android.yangke.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * author: yangke on 2018/11/8
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 封装了 emptyView
 */
public abstract class AbsBaseAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, BaseViewHolder> {

    public AbsBaseAdapter(int layoutResId, @Nullable List data) { super(layoutResId, data); }

    @Override protected void convert(BaseViewHolder helper, T item) { }
}
