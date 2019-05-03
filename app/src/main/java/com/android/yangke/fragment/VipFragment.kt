package com.android.yangke.fragment

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.ProgressBar
import com.android.yangke.R
import com.android.yangke.base.BaseApplication
import com.android.yangke.base.BaseLazyFragment
import com.android.yangke.tool.Constant
import com.gyf.barlibrary.ImmersionBar
import com.vondear.rxtools.RxClipboardTool
import com.vondear.rxtools.RxPayTool
import com.vondear.rxtools.RxSPTool
import com.vondear.rxtools.RxWebViewTool
import com.vondear.rxtools.view.RxToast
import kotlinx.android.synthetic.main.base_webview.*
import kotlinx.android.synthetic.main.fragment_vip.*
import java.util.*

/**
 * author: yangke on 2018/5/19
 * weChat: ACE_5200125
 * email : 211yangke@gmail.com
 * desc  : 主fragment
 */
class VipFragment : BaseLazyFragment() {

    private var mWebView: WebView? = null
    private var mProgressBar: ProgressBar? = null

//        private val urls = arrayOf("https://www.cilimao.xyz")
        private val urls = arrayOf("https://www.askyaya.com")
//    private val urls = arrayOf("https://lemoncili.com")
//    private val urls = arrayOf("https://www.ciliwiki.com")
//    private val urls = arrayOf("https://lemoncili.com")
//    private val urls = arrayOf("https://www.ciliwiki.com")

    override fun setLayoutId(): Int = R.layout.fragment_vip

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniView(view)
        iniListener(view)
    }

    private fun iniListener(view: View) {
        vip_fragment_qq.setOnClickListener {
            RxClipboardTool.copyText(context, QQ_FLOCK)
            MeFragment.snakeBar(view, getString(R.string.copy_author_qq_flock_success))
        }
    }

    private fun iniView(v: View) {
        iniWebView()
        ImmersionBar.setTitleBar(activity, vip_fragment_qq)
        if (RxSPTool.isFirstOpenApp(BaseApplication.instance(), Constant.FIRST_OPEN_INDICATE)) {
            RxToast.warning(getString(R.string.toast_warm_adult_software))
        }
    }

    private fun iniWebView() {
        mWebView = view?.findViewById(R.id.main_vip_webView)
        mProgressBar = view?.findViewById(R.id.progressbar_webview)
        RxWebViewTool.initWebView(context, mWebView, mProgressBar)
        mWebView?.loadUrl(urls[Random().nextInt(urls.size)])
        if (RxPayTool.isPay(context)) {
            RxToast.error(getString(R.string.toast_no_free_number))
            mWebView?.loadUrl(urls[Random().nextInt(urls.size)])
        }
    }

    fun getWebView(): WebView? = mWebView

    companion object {
        val QQ = "QQ：1551121393" //QQ
        val QQ_FLOCK = "692699158"      //QQ群
    }
}
