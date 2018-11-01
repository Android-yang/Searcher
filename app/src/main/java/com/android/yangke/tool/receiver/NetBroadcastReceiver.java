package com.android.yangke.tool.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.android.yangke.base.BaseActivity;
import com.vondear.rxtools.RxNetTool;

/**
 * author: yangke on 2018/11/1
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 网络监听广播
 */
public class NetBroadcastReceiver extends BroadcastReceiver {

    private NetChangeListener listener = BaseActivity.mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            int netWorkState = RxNetTool.getNetWorkType(context);
            if (listener != null) {
                listener.onNetworkChangeListener(netWorkState);
            }
        }
    }

    public interface NetChangeListener {
        void onNetworkChangeListener(int status);
    }

}
