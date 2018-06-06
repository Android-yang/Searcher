package com.android.yangke.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.yangke.R;
import com.vondear.rxtools.view.dialog.RxDialog;

/**
 * author: yangke on 6/6/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 点击右上角 X 图标才能关闭的dialog
 */
public class RightClickCancelDialog extends RxDialog {
    private TextView mTvTitle;
    private TextView mTvContent;
    private ImageView mIvLogo;

    public RightClickCancelDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    private void initView() {
        View dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_right_click_cancel, null);
        mTvTitle = (TextView) dialog_view.findViewById(R.id.txt_title);
        mTvContent = (TextView) dialog_view.findViewById(R.id.txt_msg);
        mIvLogo = (ImageView) dialog_view.findViewById(R.id.iv_close);
        setContentView(dialog_view);
        closeDialog();
    }

    public RightClickCancelDialog setTitle(String str) {
        mTvTitle.setText(str);
        return this;
    }

    public RightClickCancelDialog cancelable(boolean bool) {
        setCancelable(bool);
        return this;
    }

    public RightClickCancelDialog setMsg(String str) {
        mTvContent.setText(str);
        return this;
    }

    public RightClickCancelDialog setTitleColor(int color) {
        mTvTitle.setTextColor(color);
        return this;
    }

    public RightClickCancelDialog setMsgColor(int color) {
        mTvContent.setTextColor(color);
        return this;
    }

    public void closeDialog() {
        mIvLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
