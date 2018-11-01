package com.android.yangke.tool;

import android.view.View;

/**
 * author: yangke on 2018/11/1
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  :
 */
public class ViewTool {
    public static final float ALPHA_LOW = 0.5f;
    public static final float ALPHA_HIGH = 1;

    /**
     * set view can click
     *
     * @param views views
     */
    public final static void btnClickable(View... views) {
        if (views.length > 0) {
            for (View v : views) {
                v.setEnabled(true);
                v.setAlpha(ALPHA_HIGH);
            }
        }
    }

    /**
     * set view no click
     *
     * @param views views
     */
    public final static void btnNoClickable(View... views) {
        if (views.length > 0) {
            for (View v : views) {
                v.setEnabled(false);
                v.setAlpha(ALPHA_LOW);
            }
        }
    }

    /**
     * set view visible
     *
     * @param views views
     */
    public final static void setViewVisible(View... views) {
        if (views.length > 0) {
            for (View v : views) {
                v.setVisibility(v.VISIBLE);
            }
        }
    }

    /**
     * set view inVisible
     *
     * @param views views
     */
    public final static void setViewInVisible(View... views) {
        if (views.length > 0) {
            for (View v : views) {
                v.setVisibility(v.INVISIBLE);
            }
        }
    }

    /**
     * set view gone
     *
     * @param views views
     */
    public final static void setViewGone(View... views) {
        if (views.length > 0) {
            for (View v : views) {
                v.setVisibility(v.GONE);
            }
        }
    }

}
