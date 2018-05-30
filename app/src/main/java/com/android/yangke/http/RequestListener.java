package com.android.yangke.http;

import com.android.yangke.base.BaseResponse;
import com.android.yangke.vo.MagnetVo;

import java.util.List;

/**
 * author: yangke on 5/30/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 网络请求监听
 */
public interface RequestListener {
    /**
     * 服务器响应成功
     * @param list 数据
     */
    void onDataReceivedSuccess(List<? extends BaseResponse> list);

    /**
     * 服务器响应失败
     */
    void onDataReceiveFailed();
}
