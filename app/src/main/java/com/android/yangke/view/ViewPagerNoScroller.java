package com.android.yangke.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * author: yangke on 2018/5/19
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 不可以左右滚动的ViewPager
 */
public class ViewPagerNoScroller extends ViewPager {

    private boolean isCanScroll = false; //是否可以滑动

    public ViewPagerNoScroller(Context context) {
        super(context);
    }

    public ViewPagerNoScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    //----------禁止左右滑动------------------
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (isCanScroll) {
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }

    }

    /**
     * 设置 是否可以滑动
     *
     * @param isCanScroll
     */
    public void setScrollble(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }
}