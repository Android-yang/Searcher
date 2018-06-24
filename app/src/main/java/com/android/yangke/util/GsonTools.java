package com.android.yangke.util;

import com.android.yangke.base.BaseResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * author: yangke on 2018/6/22
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : Gson 转 vo
 */
public class GsonTools {

    private volatile static Gson mGson = null;
    private GsonTools() { }

    public static void init() {
        instance();
    }

    public static Gson instance() {
        if(mGson == null) {
            synchronized (GsonTools.class) {
                if(mGson == null) {
                    mGson = new Gson();
                }
            }
        }
        return mGson;
    }

    /**
     * 转成json
     *
     * @param object
     * @return
     */
    public static String GsonString(Object object) {
        String gsonString = "";
        if (mGson != null) {
            gsonString = mGson.toJson(object);
        }
        return gsonString;
    }

    /**
     * 转成bean
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static BaseResponse Gson2Bean(String gsonString, Class<? extends BaseResponse> cls) {
        BaseResponse t = null;
        if (mGson != null) {
            t = mGson.fromJson(gsonString, cls);
        }
        return t;
    }

    /**
     * 转成list
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> List<T> Gson2List(String gsonString, Class<T> cls) {
        List<T> list = null;
        if (mGson != null) {
            list = mGson.fromJson(gsonString, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> Gson2ListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (mGson != null) {
            list = mGson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    /**
     * 转成map的
     *
     * @param gsonString
     * @return
     */
    public static <T> Map<String, T> Gson2Maps(String gsonString) {
        Map<String, T> map = null;
        if (mGson != null) {
            map = mGson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }
}