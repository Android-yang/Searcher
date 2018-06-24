
package com.android.yangke.http;


import com.android.yangke.base.BaseResponse;

public class ServerErrorResponse extends BaseResponse {

    private static final long serialVersionUID = 7439716004376298946L;
    private final int mStatus;
    private final String mMessage;

    private ServerErrorResponse() {
        mStatus = ResponseCode.RESPONSE_SERVER_ERROR;
        mMessage = "服务器异常";
    }

    public static final ServerErrorResponse INSTANCE = new ServerErrorResponse();
}
