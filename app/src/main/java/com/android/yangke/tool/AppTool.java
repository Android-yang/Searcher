package com.android.yangke.tool;

import com.android.yangke.BuildConfig;

/**
 * author: yangke on 5/31/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  :
 */
public class AppTool {
    public static final boolean ENVIRONMENT_RELEASE = BuildConfig.ENVIRONMENT; //打包环境

    //闪屏时长
    public static final int SPLASH_SCREEN_DURATION = ENVIRONMENT_RELEASE == true ? 3000 : 1;

}
