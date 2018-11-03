package com.android.yangke.tool

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.support.v4.app.FragmentActivity
import android.widget.ImageView
import com.android.yangke.base.BaseApplication
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager


/**
 * author: yangke on 2018/11/3
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  :
 */
object ImageTool {
    fun loadInto (ctx: Any, imageUrl: Int, image: ImageView){
        getRequestManager(ctx)
                .load(imageUrl)
                .into(image)
    }

    fun loadIntoWithoutScaleType (ctx: Any, imageURL: String, errorImageId: Int, image: ImageView) {
        getRequestManager(ctx)
                .load(imageURL)
                .asBitmap()
                .fitCenter()
                .placeholder(errorImageId)
                .into(image)
    }

    private fun getRequestManager(obj: Any): RequestManager {
        if (obj is Fragment) {
            return Glide.with(obj)
        } else if (obj is FragmentActivity) {
            return Glide.with(obj as Activity)
        } else if (obj is Activity) {
            return Glide.with(obj)
        } else if (obj is Context) {
            return Glide.with(obj)
        } else {
            return Glide.with(BaseApplication.instance())
        }
    }
}