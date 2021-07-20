package com.example.libcommon.ui

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.libcommon.R
import com.example.libcommon.databinding.FragmentWebBinding
import com.example.libcommon.router.PagePath
import com.example.libcommon.router.RouterConstants
import com.example.libcore.mvvm.BaseVMFragment

@Route(path = PagePath.WEB_FRAGMENT)
class WebFragment : BaseVMFragment<FragmentWebBinding>(R.layout.fragment_web) {
    @Autowired(name = RouterConstants.WEB_TITLE)
    @JvmField
    public var title: String? = ""

    @Autowired(name = RouterConstants.WEB_URL)
    @JvmField
    public var url: String? = null

    override fun initView() {
        url?.let { binding.webView.loadUrl(it) }
    }
}