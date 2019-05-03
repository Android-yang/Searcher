package com.android.yangke.wxapi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.yangke.R;
import com.android.yangke.activity.MainActivity;
import com.android.yangke.activity.SearchResultActivity;
import com.android.yangke.base.BaseActivity;
import com.android.yangke.tool.Constant;
import com.android.yangke.tool.ViewTool;
import com.android.yangke.view.ImageDialog;
import com.android.yangke.view.RightClickCancelDialog;
import com.android.yangke.view.RxLoadingView;
import com.android.yangke.view.ShareDialog;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.vondear.rxtools.RxAppTool;
import com.vondear.rxtools.RxClipboardTool;
import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.RxPayTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.RxThreadPoolTool;
import com.vondear.rxtools.module.wechat.share.WechatShareModel;
import com.vondear.rxtools.module.wechat.share.WechatShareTools;
import com.vondear.rxtools.view.RxQRCode;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialogLoading;

import butterknife.BindView;
import butterknife.OnClick;

import static com.vondear.rxtools.RxTool.getContext;

/**
 * author: yangke on 6/6/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 分享APP得福利
 */
public class WXEntryActivity extends BaseActivity implements View.OnClickListener, IWXAPIEventHandler {

    public static final String KEY_SHARE_COUNT = "share_count";
    private static final String WX_APP_ID = "wxcff97bee31f78c1f";
    //单次分享成功可获取的免费次数
    private static final int SHARE_SUCCESS_AVAILABLE = 200;
    @BindView(R.id.share_txt_share_count)
    TextView mTxtShareCount;
    @BindView(R.id.share_txt_free_count)
    TextView mTxtFreeCount;
    private ShareDialog mShareDialog;
    private IWXAPI mWXAPI;
    private String APP_SHARE_URL = null;
    private RxLoadingView mLoadingQRCodeLoadingView;
    private ImageDialog mLoadingQRCodeView;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_share;
    }

    @Override
    protected void initData() {
        APP_SHARE_URL = TextUtils.isEmpty(RxSPTool.getString(getContext(), MainActivity.KEY_APK_URL))
                ? MainActivity.DEFAULT_APK_URL
                :RxSPTool.getString(getContext(), MainActivity.KEY_APK_URL);

        WechatShareTools.init(this, WX_APP_ID);
        //固定写法
        mWXAPI = WechatShareTools.getIwxapi();
        mWXAPI.handleIntent(getIntent(), this);
    }

    @Override
    protected void initView() {
        setTileLeft(getString(R.string.title_share));
        setTitleRight("", getResources().getDrawable(R.drawable.icon_share));
        setToolbarLineVisible();
        iniShareCountFreeCount();
    }

    @OnClick({R.id.share_txt_reward, R.id.share_txt_share_app, R.id.share_txt_share_qr})
    public void click(View v) {
        switch (v.getId()) {
            //奖励规则
            case R.id.share_txt_reward:
                RightClickCancelDialog dialog = new RightClickCancelDialog(this, 0);
                dialog.setTitle(getString(R.string.share_award_rule))
                        .cancelable(false)
                        .setMsg("1、将此 APP 成功分享到朋友圈，您可得" + SHARE_SUCCESS_AVAILABLE + "次使用机会。\n\n" +
                                "2、通过不正当手段获取的奖励，作者有权撤销奖励及相关交易。\n\n" +
                                "3、最终解释权归作者所有。\n\n");//最后追加两个换行是为了页面更加好看
                dialog.getWindow().setWindowAnimations(R.style.DialogEnterOut3);
                dialog.show();
                break;
            //分享app
            case R.id.share_txt_share_app:
                showShareDialog();
                break;
            //我的邀请二维码
            case R.id.share_txt_share_qr:
                showQRCodeView();
                break;
        }
    }

    private void showQRCodeView() {
        mLoadingQRCodeLoadingView = new RxLoadingView(this);
        mLoadingQRCodeLoadingView.show();
        mLoadingQRCodeView = new ImageDialog(WXEntryActivity.this, 0);

        new Thread(){
            @Override
            public void run() {
                RxQRCode.builder(APP_SHARE_URL)
                        .backColor(getResources().getColor(R.color.white))
                        .codeColor(getResources().getColor(R.color.black))
                        .codeSide(900)
                        .into(mLoadingQRCodeView.getIvContent());
                mLoadingQRCodeView.setDescription(getString(R.string.toast_search_significance));
                mHandler.sendEmptyMessage(Constant.EMPTY_MESSAGE);
            }
        }.start();
    }

    @Override
    protected void onRightButtonClick() {
        showShareDialog();
    }

    @Override
    protected void onHandleMessage(Message msg) {
        if(msg.what == Constant.EMPTY_MESSAGE) {
            RxLogTool.d("---------"+Thread.currentThread().getName());
            ViewTool.INSTANCE.dismissDialog(mLoadingQRCodeLoadingView);
            mLoadingQRCodeView.show();
        }
    }

    private void showShareDialog() {
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
                RxClipboardTool.copyText(this, APP_SHARE_URL);
                RxToast.normal(getString(R.string.toast_copy_success));
                break;
            case R.id.share_txt_weibo:
                RxToast.warning(getString(R.string.toast_in_develoment));
                break;
            //微信朋友圈
            case R.id.share_txt_weichat:
                weChatShare(WechatShareTools.SharePlace.Zone);
                break;
            //微信好友
            case R.id.share_txt_weichat_friend:
                weChatShare(WechatShareTools.SharePlace.Friend);
                break;
        }
    }

    private void weChatShare(WechatShareTools.SharePlace sharePlace) {
        if (!WechatShareTools.isWXAppInstalled()) {
            RxToast.error(getString(R.string.toast_wechat_no_installed));
            return;
        }
        String title = getString(R.string.share_search_title);
        String description = getString(R.string.share_search_description);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        byte[] bitmapByte = RxImageTool.bitmap2Bytes(bitmap, Bitmap.CompressFormat.PNG);
        WechatShareModel mWechatShareModel = new WechatShareModel(APP_SHARE_URL, title, description, bitmapByte);
        WechatShareTools.shareURL(mWechatShareModel, sharePlace);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //固定写法
        setIntent(intent);
        mWXAPI.handleIntent(intent, this);
        iniShareCountFreeCount();
    }

    /**
     * 初始化分享次数，免费次数
     */
    private void iniShareCountFreeCount() {
        int shareCountTemp = RxSPTool.getInt(this, KEY_SHARE_COUNT);
        int shareCount = shareCountTemp == -1 ? 0 : shareCountTemp;
        mTxtShareCount.setText(getString(R.string.toast_accumulative_share_num) + shareCount + getString(R.string.toast_num));
        mTxtFreeCount.setText(getString(R.string.toast_fress_num) + shareCount * SHARE_SUCCESS_AVAILABLE + getString(R.string.toast_num));
    }

    @Override
    public void onReq(BaseReq baseReq) {
        RxLogTool.d(baseReq);
    }

    /**
     * 微信请求后的响应回调
     *
     * @param baseResp
     */
    @Override
    public void onResp(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                int usedCount = RxSPTool.getInt(this, RxPayTool.KEY_USED_COUNT);
                int currentFreeCount = getCurrentFreeCount();
                if(usedCount == -1) {
                    RxToast.warning(getString(R.string.toast_try_use));
                    return;
                }
                int available = currentFreeCount + SHARE_SUCCESS_AVAILABLE;
                //对分享获取免费次数进行控制
                if (available > 130) {
                    RxToast.warning(getString(R.string.toast_share_max));
                    return;
                }
                handleShareCount();
                String shareSuccessHint = getString(R.string.toast_share_success_free_num) + SHARE_SUCCESS_AVAILABLE + getString(R.string.toast_num);
                RxToast.warning(shareSuccessHint);
                RxSPTool.putInt(this, RxPayTool.KEY_ALL_COUNT, available);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                RxToast.warning(getString(R.string.share_cancel));
                break;
        }
    }

    /**
     * 分享成功后记录分享次数
     */
    private void handleShareCount() {
        int shareCountTemp = RxSPTool.getInt(this, KEY_SHARE_COUNT);
        int shareCount = (shareCountTemp == -1 ? 0 : shareCountTemp);
        shareCount++;
        RxSPTool.putInt(this, KEY_SHARE_COUNT, shareCount);
    }

    /*
    * 剩余免费次数
    */
    private int getCurrentFreeCount() {
        return RxSPTool.getInt(getContext(), RxPayTool.KEY_ALL_COUNT);
    }
}
