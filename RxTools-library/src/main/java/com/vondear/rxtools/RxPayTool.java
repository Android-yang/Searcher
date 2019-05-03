package com.vondear.rxtools;

import android.content.Context;

/**
 * author: yangke on 2019-05-03
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  :
 */
public class RxPayTool {
    public static final String KEY_USED_COUNT = "used_count";
    public static final String KEY_ALL_COUNT  = "all_count";
    public static final int FREE_COUNT        = 400 + 1;//首次免费80;

    /**
     * 是否需要支付
     *
     * @return
     */

    public static boolean isPay(Context context) {
        int usedCountTemp = RxSPTool.getInt(context, KEY_USED_COUNT);
        int usedCount = (usedCountTemp == -1) ? 0 : usedCountTemp;
        int freeCount = getFreeCount(context);
        if (usedCount >= freeCount) {
            return true;
        }
        ++usedCount;
        RxSPTool.putInt(context, KEY_USED_COUNT, usedCount);
        RxSPTool.putInt(context, KEY_ALL_COUNT, getFreeCount(context));
        return false;
    }

    /*
     * 剩余免费次数
     */
    private static int getFreeCount(Context context) {
        int allCount = RxSPTool.getInt(context, KEY_ALL_COUNT);
        if (-1 == allCount){ return FREE_COUNT; } else return allCount;
    }

}
