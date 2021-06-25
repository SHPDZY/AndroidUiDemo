package com.example.zyuidemo.router

import android.app.Activity
import android.net.Uri
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter

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

    fun goFragment(path: String) {
        fragmentPage(path).go()
    }

    fun <T> navigation(service: Class<out T>?): T {
        return ARouter.getInstance().navigation(service)
    }

    fun navigation(url: String?) {
        val uri = Uri.parse(url)
        getPostcard(uri).go()
    }

    fun navigation(url: String?, activity: Activity?, requestCode: Int) {
        val uri = Uri.parse(url)
        getPostcard(uri).navigation(activity, requestCode)
    }
}