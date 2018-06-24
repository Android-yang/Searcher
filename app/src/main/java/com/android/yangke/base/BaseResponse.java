package com.android.yangke.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author: yangke on 5/30/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 响应类基类
 */
public class BaseResponse implements Serializable {
    //服务器返回的状态码
    @SerializedName("status")
    public int mStatus;

    //服务器返回的描述信息
    @SerializedName("msg")
    public String mMessage;

    public static final BaseResponse NULL = new BaseResponse() {
        private static final long serialVersionUID = -1057834305902264760L;
    };
}
