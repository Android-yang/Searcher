package com.android.yangke.tool;

import java.io.Serializable;

/**
 * author: yangke on 2018/6/18
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 常量类
 */
public class Constant implements Serializable {
    public static final String KEY_VIP = "key_vip";
    public static final String KEY_VISIT = "key_visit";

    public static  boolean LOG_LIFECYCLE = (AppTool.ENVIRONMENT_RELEASE == true ? false : true);

}
