package com.android.yangke.adapter;

import android.support.annotation.Nullable;

import com.android.yangke.R;
import com.android.yangke.vo.MagnetVo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * author: yangke on 5/30/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 磁力搜索结果
 */
public class MagnetAdapter extends BaseQuickAdapter<MagnetVo, BaseViewHolder> {

    public MagnetAdapter(int layoutResId, @Nullable List<MagnetVo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MagnetVo item) {
        helper.setText(R.id.search_result_tv_title, item.mTitle);
        helper.setText(R.id.search_result_tv_create_time_, item.mDate);
        helper.setText(R.id.search_result_tv_file_size_, item.mSize);
    }

}
