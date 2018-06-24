package com.android.yangke.http;

public class APKConfig {

    /**
     * 为了方便打包 将一些变量提取出来统一设置
     * 注意此项最后生效，除非设置CUSTOM
     */
//    public final static APK whichAPK;
    /**
     * 是否开启.so验证
     * 校验apk签名，是否debug 是否模拟器等功能
     * release时为true
     */
    public static boolean isOpenValidate = false;
    /**
     * 是否开启log
     * release时为false
     */
    public static boolean isOpenLog = HttpPar.COMMON_SERVER_HOST.equals(HttpPar.SERVER_HOST_UAT) ? true : false;
    /**
     * 选择不同的服务器地址
     * release时为Env.PRODUCT
     */
    public static Env which = Env.UAT;
    /**
     * 是否打开https例如内上线的不需要https
     * release时为true
     */
    public static boolean isOpenHttps = true;
    /**
     * 是否可以更改服务器地址
     * release时为false
     */
    public static boolean isChangeUrl = false;
    /**
     * 是否开启挡板数据
     */
    public static boolean isTailgateOn = false;

//    static {
//        whichAPK = APK.UAT;
//    }
//
//    static {
//        switch (whichAPK) {
//            case PRODUCT:
//                isOpenValidate = true;
//                isOpenLog = false;
//                which = Env.PRODUCT;
//                isOpenHttps = true;
//                isChangeUrl = false;
//                break;
//            case UAT:
//                isOpenValidate = false;
//                isOpenLog = true;
//                which = Env.UAT;
//                isOpenHttps = true;
//                isChangeUrl = true;
//                isTailgateOn = false;
//                break;
//            default:
//                break;
//        }
//    }

    public enum APK {
        /**
         * 线上打包的设置
         */
        PRODUCT,
        /**
         * 开发测试打包的设置
         */
        UAT,
    }
}
