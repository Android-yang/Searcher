package com.android.yangke.http;

import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * author: yangke on 5/30/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 网络请求监听
 */
public interface RequestListener extends RequestListenerSwitcher {
    /**
     * 服务器响应成功
     * @param list 数据
     */
    void onDataReceivedSuccess(List<?> list);

    /**
     * 服务器响应失败
     */
    void onDataReceiveFailed();


    public void onRequestStart(NetworkTask task);

    public void onRequestEnd(NetworkTask task);

    public void onNetError(NetworkTask task);

    public void onRequestSuccess(NetworkTask task);

    public void onRequestFail(NetworkTask task);

    /**
     * 不管服务器返回失败（status != 0)还是网络问题都会触发的回调
     */
    public void onFail(NetworkTask task);

    public static final RequestListener EMPTY_LISTENER = new RequestListener() {

        @Override
        public void onDataReceivedSuccess(List<?> list) {
        }

        @Override
        public void onDataReceiveFailed() {
        }

        @Override
        public void onRequestStart(NetworkTask task) {
            Logger.d("EMPTY_RequestListener-onRequestStart");
        }

        @Override
        public void onRequestEnd(NetworkTask task) {
            Logger.d("EMPTY_RequestListener-onRequestEnd");
        }

        @Override
        public void onNetError(NetworkTask task) {
            Logger.d("EMPTY_RequestListener-onNetError");
        }

        @Override
        public void onRequestSuccess(NetworkTask task) {
            Logger.d("EMPTY_RequestListener-onRequestSuccess");
        }

        @Override
        public void onRequestFail(NetworkTask task) {
            Logger.d("EMPTY_RequestListener-onRequestFail");
        }

        @Override
        public void onFail(NetworkTask task) {
            Logger.d("EMPTY_RequestListener-onFail");
        }

        @Override
        public void openListener() {
        }

        @Override
        public void closeListener() {
        }

        @Override
        public boolean isListenerOn() {
            return true;
        }

    };
}
