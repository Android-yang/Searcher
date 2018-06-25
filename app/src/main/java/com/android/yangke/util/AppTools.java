package com.android.yangke.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * author: yangke on 5/31/18.
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  :
 */
public class AppTools {
    //闪屏时长
    public static final int SPLASH_SCREEN_DURATION = 3000;

    /**
     *
     * @param context context
     * @param packageName 应用报名
     * @return true 标志安装了此 APP，false 反之
     */
    public static boolean appIsInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        if (packageInfos != null) {
            for (PackageInfo packageInfo : packageInfos) {
                if(packageInfo.packageName.equals(packageName)){
                    return true;
                }
            }
        }
        return false;
    }
}
