package com.android.yangke.tool

import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.TextView

/**
 * author: yangke on 2018/11/1
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  :
 */
object ViewTool {
    val ALPHA_LOW = 0.5f
    val ALPHA_HIGH = 1f

    /**
     * set view can click
     *
     * @param views views
     */
    fun btnClickable(vararg views: View) {
        if (views.size > 0) {
            for (v in views) {
                v.isEnabled = true
                v.alpha = ALPHA_HIGH
            }
        }
    }

    /**
     * set view no click
     *
     * @param views views
     */
    fun btnNoClickable(vararg views: View) {
        if (views.size > 0) {
            for (v in views) {
                v.isEnabled = false
                v.alpha = ALPHA_LOW
            }
        }
    }

    /**
     * set view visible
     *
     * @param views views
     */
    fun setViewVisible(vararg views: View) {
        if (views.size > 0) {
            for (v in views) {
                v.visibility = View.VISIBLE
            }
        }
    }

    /**
     * set view inVisible
     *
     * @param views views
     */
    fun setViewInVisible(vararg views: View) {
        if (views.size > 0) {
            for (v in views) {
                v.visibility = View.INVISIBLE
            }
        }
    }

    /**
     * set view gone
     *
     * @param views views
     */
    fun setViewGone(vararg views: View) {
        if (views.size > 0) {
            for (v in views) {
                v.visibility = View.GONE
            }
        }
    }

    /**
     * @param textView textView
     * @param ctx      context
     * @param fontName 字体名字，字体文件放在 assets 根目录。例：方正字体
     */
    fun textSetTypeface(textView: TextView, ctx: Context, fontName: String) {
        val typeface = Typeface.createFromAsset(ctx.assets, fontName)
        textView.typeface = typeface
    }

    fun dismissDialog(dia: Dialog) {
        if (null != dia ) {
            dia.dismiss()
        }
    }

}
