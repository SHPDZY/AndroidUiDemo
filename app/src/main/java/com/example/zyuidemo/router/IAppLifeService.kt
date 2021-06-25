package com.example.zyuidemo.router


import android.app.Activity
import com.alibaba.android.arouter.facade.template.IProvider
import com.alibaba.android.arouter.launcher.ARouter

fun topActivity(): Activity? {
    return ARouter.getInstance().navigation(IAppLifeService::class.java).getTopActivity()
}
interface IAppLifeService : IProvider {

    fun getTopActivity(): Activity?

}