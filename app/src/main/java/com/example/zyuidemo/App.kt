package com.example.zyuidemo

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.Utils
import com.example.zyuidemo.base.activitylifecycle.ActivityLifecycleManager
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.core.ImageTranscoderType
import com.facebook.imagepipeline.core.MemoryChunkType
import com.hjq.permissions.XXPermissions
import com.jeremyliao.liveeventbus.LiveEventBus
import com.tencent.mmkv.MMKV

/**
 * author: zy
 * time  : 2021/4/19
 * desc  :
 */
class App : Application() {
    @RequiresApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        // MMKV 初始化
        MMKV.initialize(this)
        // Arouter 初始化
        ARouter.init(this)
        // Fresco 初始化
        initFresco()
        XXPermissions.setScopedStorage(true)
        // LiveEventBus 初始化
        LiveEventBus.config().lifecycleObserverAlwaysActive(false).autoClear(true)
        ActivityLifecycleManager.getInstance().init()
    }

    private fun initFresco() {
        Fresco.initialize(
            applicationContext,
            ImagePipelineConfig.newBuilder(applicationContext)
                .setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
                .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
                .experiment().setNativeCodeDisabled(true)
                .build()
        )
    }
}