package com.example.libcommon.base

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.Utils
import com.example.libcommon.base.activitylifecycle.ActivityLifecycleManager
import com.hjq.permissions.XXPermissions
import com.jeremyliao.liveeventbus.LiveEventBus
import com.tencent.mmkv.MMKV

/**
 * author: zy
 * time  : 2021/4/19
 * desc  :
 */
open class BaseApplication : Application() {
    @RequiresApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        // MMKV 初始化
        MMKV.initialize(this)
        // Arouter 初始化
        ARouter.init(this)
        XXPermissions.setScopedStorage(true)
        // LiveEventBus 初始化
        LiveEventBus.config().lifecycleObserverAlwaysActive(false).autoClear(true)
        ActivityLifecycleManager.getInstance().init()
    }

}