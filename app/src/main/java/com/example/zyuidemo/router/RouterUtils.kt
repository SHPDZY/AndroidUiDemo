package com.example.zyuidemo.router

import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.arouter.facade.Postcard
import android.net.Uri
import java.lang.Class
import com.example.zyuidemo.router.RouterUtils
import android.app.Activity
import java.lang.AssertionError

object RouterUtils {

    fun getPostcard(path: String?): Postcard {
        return ARouter.getInstance().build(path)
    }

    fun getPostcard(uri: Uri?): Postcard {
        return ARouter.getInstance().build(uri)
    }

    fun fragmentPage(path: String): Postcard {
        return ARouter.getInstance().fragmentPage(path)
    }

    fun <T> navigation(service: Class<out T>?): T {
        return ARouter.getInstance().navigation(service)
    }

    fun navigation(url: String?) {
        val uri = Uri.parse(url)
        getPostcard(uri).navigation()
    }

    fun navigation(url: String?, activity: Activity?, requestCode: Int) {
        val uri = Uri.parse(url)
        getPostcard(uri).navigation(activity, requestCode)
    }
}