package com.android.yangke.base;

import java.io.Serializable;

/**
 * author: yangke on 5/30/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 响应类基类
 */
public class BaseResponse implements Serializable {
    //服务器返回的状态码
    public int mStatus = 0;

    //服务器返回的描述信息
    public String mMessage;

    //服务器返回的版本号，预留
    public String mVersion;

}
