package com.android.yangke.tool

/**
 * author: yangke on 2018/6/18
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 常量类
 */
object Constant {
    val KEY_VIP = "key_vip"
    val KEY_VISIT = "key_visit"

    val FONT_FOUNDER_SIMPLIFIED = "方正启体简体.ttf"

    var LOG_LIFECYCLE = if (AppTool.ENVIRONMENT_RELEASE == true) false else true
}
