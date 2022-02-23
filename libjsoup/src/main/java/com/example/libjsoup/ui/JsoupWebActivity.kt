package com.example.libjsoup.ui

import android.annotation.SuppressLint
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.KeyboardUtils
import com.drakeet.multitype.MultiTypeAdapter
import com.example.libcommon.beans.ItemSearchBean
import com.example.libcommon.beans.ItemSearchNavBean
import com.example.libcommon.router.PagePath
import com.example.libcommon.utils.launchDelay
import com.example.libcore.multitype.BaseViewBinder
import com.example.libcore.multitype.vu.VuCallBack
import com.example.libcore.mvvm.BaseVMActivity
import com.example.libjsoup.R
import com.example.libjsoup.adapter.SearchNavRecyclerView
import com.example.libjsoup.adapter.SearchRecyclerView
import com.example.libjsoup.databinding.ActivityJsoupBinding
import com.example.libjsoup.databinding.ActivityJsoupWebBinding
import com.example.libjsoup.vm.JsoupViewModel
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.libjsoup.ui.JsoupWebActivity.MyWebViewClient
import android.webkit.WebSettings













@Route(path = PagePath.GROUP_JSOUP_WEB_ACTIVITY)
class JsoupWebActivity : BaseVMActivity<ActivityJsoupWebBinding>(R.layout.activity_jsoup_web),
    View.OnClickListener {
    private val viewModel by lazy { ViewModelProvider(this).get(JsoupViewModel::class.java) }

    inner class JavaObjectJsInterface {
        @JavascriptInterface // 要加这个注解，不然调用不到
        fun onHtml(html: String?) {
            binding.tvHtml.text = html
            handleParseHtml(html)
        }
    }
    inner class MyWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        binding.click = this
        handleWebView()

        launchDelay {
            KeyboardUtils.showSoftInput(binding.etHtml)
        }
    }

    override fun startObserve() {

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnOpen -> {
                KeyboardUtils.hideSoftInput(binding.etHtml)
                handleWebView()
            }
            binding.btnParseHtml -> {
                binding.webView.loadUrl("javascript:window.java_obj.onHtml('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');")
            }
        }
    }

    private fun handleWebView() {
        binding.webView.addJavascriptInterface(JavaObjectJsInterface(), "java_obj")
        binding.webView.webViewClient = MyWebViewClient()
        val settings: WebSettings = binding.webView.settings
        settings.javaScriptEnabled = true
        binding.webView.loadUrl(binding.etHtml.text.toString())
    }

    private fun handleParseHtml(url: String?) {
        viewModel.parseHtml(url.toString())
    }

    private var xPopup: BasePopupView? = null

    private fun showDialog() {
        if (xPopup == null) {
            xPopup = XPopup.Builder(this).asLoading()
        }
        xPopup?.show()
    }

    private fun dismissDialog() {
        xPopup?.smartDismiss()
    }

}