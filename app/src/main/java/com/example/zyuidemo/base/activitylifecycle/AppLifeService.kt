package com.example.zyuidemo.base.activitylifecycle

import android.app.Activity
import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.zyuidemo.router.IAppLifeService
import com.example.zyuidemo.router.ServicePath

@Route(path = ServicePath.APPLIFE_SERVICE)
class AppLifeService : IAppLifeService {
    override fun getTopActivity(): Activity? {
        return ForegroundLifecycleCallback.getInstance().topActivity
    }

    override fun init(context: Context?) {
    }
}