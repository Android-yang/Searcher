package com.android.yangke.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.android.yangke.R;
import com.android.yangke.base.BaseActivity;
import com.android.yangke.view.RightClickCancelDialog;
import com.android.yangke.view.ShareDialog;
import com.vondear.rxtools.RxAppUtils;
import com.vondear.rxtools.RxImageUtils;
import com.vondear.rxtools.model.wechat.share.WechatShareModel;
import com.vondear.rxtools.model.wechat.share.WechatShareTools;
import com.vondear.rxtools.view.RxToast;

import butterknife.OnClick;

/**
 * author: yangke on 6/6/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 邀请好友得福利
 */
public class ShareActivity extends BaseActivity implements View.OnClickListener {

    private ShareDialog mShareDialog;
    private static final String WX_APP_ID = "wxcff97bee31f78c1f";

    @Override
    protected int setLayoutId() {
        return R.layout.activity_share;
    }

    @Override
    protected void initData() {
        WechatShareTools.init(this, WX_APP_ID);
    }

    @Override
    protected void initView() {
        setTileLeft(getString(R.string.title_share));
        setTitleRight("", getDrawable(R.drawable.icon_share));
        setToolbarLineVisible();
    }

    @OnClick(R.id.share_txt_reward)
    public void click(View v) {
        switch (v.getId()) {
            //奖励规则
            case R.id.share_txt_reward:
                RightClickCancelDialog dialog = new RightClickCancelDialog(this, 0);
                dialog.setTitle(getString(R.string.share_award_rule))
                        .cancelable(false)
                        .setMsg("1、将此 APP 成功分享到朋友圈，您可得80次使用机会。\n\n" +
                                "2、通过不正当手段获取的奖励，作者有权撤销奖励及相关交易。\n\n" +
                                "3、最终解释权归作者所有。\n\n");//最后追加两个换行是为了页面更加好看
                dialog.getWindow().setWindowAnimations(R.style.DialogEnterOut2);
                dialog.show();
                break;
        }
    }

    @Override
    protected void onRightButtonClick() {
        mShareDialog = new ShareDialog(this, 0);
        mShareDialog.mWeiChatFriend.setOnClickListener(this);
        mShareDialog.mWeibo.setOnClickListener(this);
        mShareDialog.mWeichat.setOnClickListener(this);
        mShareDialog.mCopyHref.setOnClickListener(this);
        mShareDialog.setWindowAnimations(R.style.DialogEnterOut1);
        mShareDialog.show();
    }

    @Override
    public void onClick(View v) {
        mShareDialog.dismiss();
        switch (v.getId()) {
            case R.id.share_txt_copy_href:
                RxToast.normal("href");
                break;
            case R.id.share_txt_weibo:
                RxToast.normal("weibo");
                break;
            case R.id.share_txt_weichat:
                RxToast.normal("weichat");
                break;
            case R.id.share_txt_weichat_friend:
//                String url = "https://www.baidu.com";
//                String description = "me description";
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.close);
//                byte[] bitmapByte = RxImageUtils.bitmap2Bytes(bitmap, Bitmap.CompressFormat.PNG);
//                WechatShareModel mWechatShareModel = new WechatShareModel(url, "title", description, bitmapByte);
//                WechatShareTools.shareURL(mWechatShareModel, WechatShareTools.SharePlace.Friend);
                break;
        }
    }
}
