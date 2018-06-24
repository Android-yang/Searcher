
package com.android.yangke.http;


import com.android.yangke.base.BaseResponse;

public final class NetErrorResponse extends BaseResponse {

    private static final long serialVersionUID = 5042601712871128263L;

    private NetErrorResponse() {
        mStatus = ResponseCode.NET_ERROR;
        mMessage = "网络连接异常，请稍后再试";
    }

    public static NetErrorResponse INSTANCE = new NetErrorResponse();
}
