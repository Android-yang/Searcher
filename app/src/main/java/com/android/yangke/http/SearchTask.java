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
    private RxDialogLoading mLoading;
    private Context mContext;

    /**
     * 设置网络请求监听器
     *
     * @param listener 监听器
     */
    public void setRequestListener(RequestListener listener, Context context) {
        this.mRequestListener = listener;
        this.mContext = context;
    }

    //TODO 此处为硬编码后续考虑优化
    private void iniLoadingDialog() {
        mLoading = new RxDialogLoading(mContext);
        mLoading.setCancelable(false);
        mLoading.setLoadingText("加载中...");
        mLoading.show();
    }


    /**
     * 此函数在 doInBackground 之前执行，且运行在 UI 线程
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        iniLoadingDialog();
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
        TorrentKittySearch search = new TorrentKittySearch();
        List<MagnetVo> searchResultList = search.getResult(keyword);
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
        mLoading.dismiss();
        if (responses != null) {
            mRequestListener.onDataReceivedSuccess(responses);
        } else {
            mRequestListener.onDataReceiveFailed();
        }
    }

}
