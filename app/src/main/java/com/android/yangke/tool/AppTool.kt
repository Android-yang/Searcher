package com.android.yangke.tool

import com.android.yangke.BuildConfig

/**
 * author: yangke on 5/31/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  :
 */
object AppTool {

    //打包环境
    val ENVIRONMENT_RELEASE = BuildConfig.ENVIRONMENT
    //闪屏时长
    val SPLASH_SCREEN_DURATION = if (ENVIRONMENT_RELEASE == true) 3000 else 1
}
