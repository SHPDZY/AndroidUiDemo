package com.example.libscan.ui

import android.os.Bundle
import android.os.PersistableBundle
import com.journeyapps.barcodescanner.CaptureActivity
import com.pgyersdk.crash.PgyCrashManager

/**
 * @author  : zhangyong
 * @date    : 2023/8/11
 * @desc    :
 * @version :
 */
class MyCaptureActivity: CaptureActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        PgyCrashManager.register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        PgyCrashManager.unregister()
    }
}