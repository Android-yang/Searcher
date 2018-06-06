package com.android.yangke.activity;

import android.view.View;

import com.android.yangke.R;
import com.android.yangke.base.BaseActivity;
import com.android.yangke.view.RightClickCancelDialog;

import butterknife.OnClick;

/**
 * author: yangke on 6/6/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 邀请好友得福利
 */
public class ShareActivity extends BaseActivity {

    @Override
    protected int setLayoutId() {
        return R.layout.activity_share;
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
                dialog.show();
                break;
        }
    }
}
