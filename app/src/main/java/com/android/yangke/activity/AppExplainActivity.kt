package com.android.yangke.activity

import android.content.Context
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.android.yangke.R
import com.android.yangke.base.BaseActivity
import com.android.yangke.tool.Constant
import com.android.yangke.tool.ImageTool
import com.vondear.rxtools.RxActivityTool
import com.vondear.rxtools.RxSPTool

/**
 * author: yangke on 2018/11/3
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : App explain activity
 */
class AppExplainActivity : BaseActivity() {

    private lateinit var mViewPager: ViewPager
    private lateinit var mDataList: ArrayList<Int>
    private lateinit var mSectionsPagerAdapter: ExplainPagerAdapter

    override fun setLayoutId(): Int = R.layout.activity_app_explain

    override fun initData() {
        RxSPTool.putBoolean(this, Constant.FIRST_OPEN_APP, false)
        mViewPager = findViewById(R.id.app_explain_viewPager) as ViewPager
        mDataList = ArrayList<Int>()
        mDataList!!.add(R.mipmap.explain1)
        mDataList!!.add(R.mipmap.explain4)
        mDataList!!.add(R.mipmap.explain3)
        mDataList!!.add(R.mipmap.explain2)
        mSectionsPagerAdapter = ExplainPagerAdapter(this, mDataList!!, false)
    }

    override fun initView() {
        setSwipeBackEnable(false)
        hideTitleBar()
        mViewPager!!.adapter = mSectionsPagerAdapter
    }

    fun doClick(v: View) {
        RxActivityTool.skipActivity(this, MainActivity::class.java)
        finish()
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class ExplainPagerAdapter(val context: Context, list: List<*>, isLoop: Boolean) : PagerAdapter() {
        var mDataList = ArrayList<Int>()
        var mIsLoop = false

        init {
            this.mDataList = list as ArrayList<Int>
            this.mIsLoop = isLoop
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return if (mIsLoop == false) mDataList.size else Integer.MAX_VALUE
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            var imageView: ImageView? = null
            if (imageView == null) {
                imageView = ImageView(context)
            }
            ImageTool.loadInto(context, mDataList[position % mDataList.size], imageView)
            container.addView(imageView)
            return imageView
        }
    }

}
