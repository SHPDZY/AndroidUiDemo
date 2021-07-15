package com.example.zyuidemo

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.libcommon.base.BaseApplication
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.core.ImageTranscoderType
import com.facebook.imagepipeline.core.MemoryChunkType

/**
 * author: zy
 * time  : 2021/4/19
 * desc  :
 */
class MyApplication : BaseApplication() {
    @RequiresApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    override fun onCreate() {
        super.onCreate()
        // Fresco 初始化
        initFresco()
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