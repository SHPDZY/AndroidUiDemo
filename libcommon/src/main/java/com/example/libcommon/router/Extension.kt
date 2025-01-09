package com.example.libcommon.router

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.core.LogisticsCenter
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ActivityUtils

/**
 * @date: 2021-02-02 1:49 PM Tuesday
 */

fun loginService(): ILoginService {
    return ARouter.getInstance().navigation(ILoginService::class.java)
}

fun jsoupService(): IJsoupService? {
    return ARouter.getInstance().navigation(IJsoupService::class.java)
}

fun scanService(): IScanService? {
    return ARouter.getInstance().navigation(IScanService::class.java)
}

fun topActivity(): Activity? {
    return ARouter.getInstance().navigation(IAppLifeService::class.java).getTopActivity()
}

fun openWeb(url: String?, title: String? = "") {
    RouterUtils.fragmentPage(PagePath.WEB_FRAGMENT).withString(RouterConstants.WEB_URL, url).go()
}

fun Postcard.go() {
    var topActivity = topActivity()
    if (topActivity != null) {
        this.navigation(topActivity)
        return
    }
    topActivity = ActivityUtils.getTopActivity()
    if (topActivity != null) {
        this.navigation(topActivity)
    }
}

fun ARouter.fragmentPage(fragmentPath: String): Postcard {
    return build(PagePath.NEVBASE_FRAGMENT_CONTAINER).withString(
        RouterConstants.EXTRA_KEY_FRAGMENT_PATH,
        fragmentPath
    )
}

fun ARouter.transFragmentPage(fragmentPath: String): Postcard {
    return build(PagePath.NEVBASE_TRANS_FRAGMENT_CONTAINER).withString(
        RouterConstants.EXTRA_KEY_FRAGMENT_PATH,
        fragmentPath
    )
}

fun ARouter.page(pagePath: String): Postcard {
    if (pagePath.isNullOrEmpty()) {
        throw IllegalArgumentException("Arouter path is invalidate")
    }
    if (pagePath.startsWith("http") || pagePath.startsWith("https")) {
        return fragmentPage(PagePath.WEB_FRAGMENT).withString(RouterConstants.WEB_URL, pagePath)
    }
    var index = pagePath.indexOf("/", 1)
    var subPath = pagePath.substring(index, pagePath.length)
    if (subPath.startsWith(PagePath.ACTIVITY_PREFIX)) {
        return build(pagePath)
    } else if (subPath.startsWith(PagePath.FRAGMENT_PREFIX)) {
        return fragmentPage(pagePath)
    }
    throw IllegalArgumentException("Arouter path is invalidate")
}

fun ARouter.transPage(pagePath: String): Postcard {
    if (pagePath.isNullOrEmpty()) {
        throw IllegalArgumentException("Arouter path is invalidate")
    }
    if (pagePath.startsWith("http") || pagePath.startsWith("https")) {
        return transFragmentPage(PagePath.WEB_FRAGMENT).withString(
            RouterConstants.WEB_URL,
            pagePath
        )
    }
    var index = pagePath.indexOf("/", 1)
    var subPath = pagePath.substring(index, pagePath.length)
    if (subPath.startsWith(PagePath.ACTIVITY_PREFIX)) {
        return build(pagePath)
    } else if (subPath.startsWith(PagePath.FRAGMENT_PREFIX)) {
        return transFragmentPage(pagePath)
    }
    throw IllegalArgumentException("Arouter path is invalidate")
}

fun Postcard.h5Title(title: String): Postcard {
    return withString(RouterConstants.WEB_TITLE, title)
}

fun Postcard.navigationForResult(fragment: Fragment, requestCode: Int) {
    LogisticsCenter.completion(this)
    val intent = Intent(fragment.activity, this.getDestination());
    intent.putExtras(this.extras)
    fragment.startActivityForResult(intent, requestCode)
}