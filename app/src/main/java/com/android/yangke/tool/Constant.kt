package com.android.yangke.tool

import android.graphics.Color

/**
 * author: yangke on 2018/6/18
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 常量类
 */
object Constant {
    const  val AUTHOR_HREF = "https://www.jianshu.com/p/3a17db598e57"
    const val FIRST_OPEN_APP = "firt_open_app"
    const val FIRST_OPEN_INDICATE = "first_open_indicate"
    val KEY_VIP = "key_vip"
    val KEY_VISIT = "key_visit"

    val FONT_FOUNDER_SIMPLIFIED = "方正启体简体.ttf"

    var LOG_LIFECYCLE = if (AppTool.ENVIRONMENT_RELEASE == true) false else true

    val COLORS = intArrayOf(
            Color.parseColor("#90C5F0"),
            Color.parseColor("#91CED5"),
            Color.parseColor("#F88F55"),
            Color.parseColor("#C0AFD0"),
            Color.parseColor("#E78F8F"),
            Color.parseColor("#67CCB7"),
            Color.parseColor("#F6BC7E")
    )
}
