package com.example.libjsoup.ui

import android.annotation.SuppressLint
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.KeyboardUtils
import com.example.libcommon.router.PagePath
import com.example.libcommon.utils.launchDelay
import com.example.libcore.mvvm.BaseVMActivity
import com.example.libjsoup.R
import com.example.libjsoup.databinding.ActivityJsoupWebBinding
import com.example.libjsoup.vm.JsoupViewModel


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

}