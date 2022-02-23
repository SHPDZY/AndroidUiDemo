package com.example.libjsoup.router

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.libcommon.router.IJsoupService
import com.example.libcommon.router.PagePath
import com.example.libcommon.router.RouterUtils
import com.example.libcommon.router.ServicePath

@Route(path = ServicePath.JSOUP_SERVICE)
class JsoupService : IJsoupService {
    override fun openJsoupActivity() {
        RouterUtils.navigation(PagePath.GROUP_JSOUP_ACTIVITY)
    }

    override fun openJsoupWebActivity() {
        RouterUtils.navigation(PagePath.GROUP_JSOUP_WEB_ACTIVITY)
    }

    override fun init(context: Context?) {
    }

}