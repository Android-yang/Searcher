package com.android.yangke.http;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * author: yangke on 5/30/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : http 请求使用的网络参数
 */
public class HttpPar implements Serializable {
    public static final String BASE_URL_TORRENT_KITTY = "https://www.torrentkitty.tv/search/";
    //服务器url
    public static final String SERVER_URL = "search.yokol.top/system";

    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";

    //请求类型
    public static String HTTP_GET = "GET";
    public static String HTTP_POST = "POST";

    //网络请求超时时间
    public static final int TIMEOUT_MILLIS = 30 * 1000;
    public static final int LONG_TIMEOUT_MILLIS = TIMEOUT_MILLIS * 2;

    //生产服务器地址
    public static String SERVER_HOST_PRODUCT = SERVER_URL;
    public static String SERVER_HOST_IMAGE_PRODUCT = "";
    public final static String SERVER_HOST_PRODUCT_HOSTNAME = "";
    public final static String SERVER_HOST_PRODUCT_PIN = "B593590883DFCD1B595FA3CF0FBD1B3B3723E0F1";

    //UAT服务器地址
    public static String SERVER_HOST_UAT = SERVER_URL;
    public static String SERVER_HOST_IMAGE_UAT = "";
    public final static String SERVER_HOST_UAT_HOSTNAME = "";
    public final static String SERVER_HOST_UAT_PIN = "C1780B65FB695E1A51DE3F1973B5AD79BDE970BD";

    //在此修改服务器地址(目前只改第一行就行);
    public static String COMMON_SERVER_HOST = SERVER_HOST_PRODUCT;
    public static String COMMON_SERVER_HOST_IMAGE = "";


    public static final String VERSION_KEY = "version";
    public static final String SOURCE_KEY = "source";
    public static final String DEVICE_KEY = "deviceid";
    public static final String SOURCE_VALUE = "1";
    public final static String API_VERSION = "1.0";
    //设备IMEI号
    public static String IMEI = "";


    public static Map<String, String> getBasicParams() {
        /*
         * always create a new map, in case of someone may modify it for further use
		 */
        Map<String, String> params = new HashMap();

//		/** 接口版本 */
//		params.put(VERSION_KEY, API_VERSION);
//		/** 渠道号 APPSTORE、ZS91、MUMAYI */
//		params.put(CHANNELCODE_KEY, ChannelCode.trim());
//		/** 机型 */
//		params.put(MODEL_KEY, MODEL.trim());
//		/** 客户端版本 */
//		params.put(CLIENTVERSION_KEY, VERSION);
//		/** 设备信息 json字符串 */
//		params.put(DEVICEINFO_KEY, JSON.toJSONString(new DeviceInfo()));
//		params.put(PLATFORM_KEY, Platform);
//        params.put(VERSION_KEY, API_VERSION);
//        params.put(DEVICE_KEY, IMEI);
//        params.put(SOURCE_KEY, SOURCE_VALUE);
        return params;
    }

}
