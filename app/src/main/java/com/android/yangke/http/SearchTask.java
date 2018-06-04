package com.android.yangke.http;

import android.content.Context;
import android.os.AsyncTask;

import com.android.yangke.base.BaseResponse;
import com.android.yangke.vo.MagnetVo;
import com.vondear.rxtools.view.dialog.RxDialogLoading;

import java.util.List;

/**
 * author: yangke on 5/30/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : get/post task
 */
public class SearchTask extends AsyncTask<String, Void, List> {

    public RequestListener mRequestListener;

    /**
     * 设置网络请求监听器
     *
     * @param listener 监听器
     */
    public void setRequestListener(RequestListener listener) {
        this.mRequestListener = listener;
    }

    /**
     * 此函数在 doInBackground 之前执行，且运行在 UI 线程
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * 此函数在子线程执行
     *
     * @param strings 网络请求参数
     * @return 服务器接收到的数据
     */
    @Override
    protected List<? extends BaseResponse> doInBackground(String... strings) {
        String keyword = strings[0];//搜索关键字
        String tempNum = strings[1];
        int page = Integer.parseInt(tempNum);
        TorrentKittySearch search = new TorrentKittySearch();
        List<MagnetVo> searchResultList = search.getRequestResult(keyword, page);
        return searchResultList;
    }

    /**
     * 网络请求接收函数
     *
     * @param responses 服务器接收到的数据
     */
    @Override
    protected void onPostExecute(List responses) {
        super.onPostExecute(responses);
        if (responses != null) {
            mRequestListener.onDataReceivedSuccess(responses);
        } else {
            mRequestListener.onDataReceiveFailed();
        }
    }

}
