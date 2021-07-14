package com.example.zyuidemo.router

import android.app.Activity
import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.libcommon.base.activitylifecycle.ForegroundLifecycleCallback
import com.example.libcommon.router.IAppLifeService
import com.example.libcommon.router.ServicePath

@Route(path = ServicePath.APPLIFE_SERVICE)
class AppLifeService : IAppLifeService {
    override fun getTopActivity(): Activity? {
        return ForegroundLifecycleCallback.getInstance().topActivity
    }

    override fun init(context: Context?) {
    }
}