package com.android.yangke.vo;

import com.android.yangke.base.BaseResponse;
import com.google.gson.annotations.SerializedName;

/**
 * author: yangke on 2018/6/22
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 检测更新
 */
public class AppVersionVo extends BaseResponse {

    /**
     * data : {"version":"1.0","url":"http://yangwenxue.gitee.io/test/personfile/search-1.0-2018-06-19-release.apk","info":"暂无描述"}
     * msg : 操作成功
     * status : 200
     */

    @SerializedName("data")
    public AppDataBean mData;

    public static class AppDataBean {
        /**
         * version : 1.0
         * url : http://yangwenxue.gitee.io/test/personfile/search-1.0-2018-06-19-release.apk
         * info : 暂无描述
         */

        @SerializedName("version")
        public int mVersion;
        @SerializedName("url")
        public String mUrl;
        @SerializedName("info")
        public String mInfo;
    }
}
