package com.android.yangke.view;

import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.yangke.R;
import com.vondear.rxtools.view.dialog.RxDialog;

/**
 * author: yangke on 6/6/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 分享dialog
 */
public class ShareDialog extends RxDialog {
    public TextView mWeiChatFriend, mCopyHref, mWeibo, mWeichat;

    public ShareDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
        setGravity(Gravity.BOTTOM);
    }

    private void initView() {
        View dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_share, null);
        mWeiChatFriend = (TextView) dialog_view.findViewById(R.id.share_txt_weichat_friend);
        mCopyHref = (TextView) dialog_view.findViewById(R.id.share_txt_copy_href);
        mWeibo = (TextView) dialog_view.findViewById(R.id.share_txt_weibo);
        mWeichat = (TextView) dialog_view.findViewById(R.id.share_txt_weichat);
        setContentView(dialog_view);

        //设置dialog左右下外边距
        Window dialogWindow = getWindow();
        Display display = dialogWindow.getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        int dialogCurrentWidth = display.getWidth();
        int displayWidth = (int) (dialogCurrentWidth * 0.95);
        p.y = (dialogCurrentWidth - displayWidth) / 2;
        p.width = displayWidth;
        dialogWindow.setAttributes(p);
    }


    public ShareDialog cancelable(boolean bool) {
        setCancelable(bool);
        return this;
    }

    public ShareDialog setGravity(int gravity) {
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(gravity);
        return this;
    }

    public ShareDialog setWindowAnimations(int style) {
        getWindow().setWindowAnimations(style);  //添加动画
        return this;
    }
}
