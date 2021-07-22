package com.example.libcommon.utils

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * 卡顿检测
 */
class MonitorManager private constructor() {
    private val delayMillis = 200L
    private var handlerThread: HandlerThread = HandlerThread("MonitorManager")
    private var handler: Handler? = null
    private var startTime = 0L
    private var monitorData: MutableLiveData<String>? = null
    private var runnable: Runnable = Runnable {
        startTime = System.currentTimeMillis()
        val sb = StringBuilder()
        val stackTrace = Looper.getMainLooper().thread.stackTrace
        stackTrace.forEach {
            sb.append("$it\n");
        }
        val stackTraceMsg = sb.toString()
        monitorData?.postValue(stackTraceMsg)
        Log.e("MonitorManager", stackTraceMsg)
    }

    init {
        handlerThread.start()
        handler = Handler(handlerThread.looper)
    }

    fun initMonitor() {
        Looper.getMainLooper().setMessageLogging {
            if (it.startsWith(">>>>> Dispatching to")) {
                start()
            } else if (it.startsWith("<<<<< Finished to")) {
                logTimeOfMethod()
                stop()
            }
        }
    }

    fun observe(owner: LifecycleOwner, observer: Observer<String>) {
        if (monitorData == null) monitorData = MutableLiveData<String>()
        monitorData?.observe(owner, observer)
    }

    private fun logTimeOfMethod() {
        if (startTime > 0) {
            val time = (System.currentTimeMillis() - startTime) + delayMillis
            Log.e("MonitorManager", "total time of method ->> $time ms")
            startTime = 0
        }
    }

    private fun start() {
        handler?.postDelayed(runnable, delayMillis)
    }

    private fun stop() {
        handler?.removeCallbacks(runnable)
    }

    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = MonitorManager()
    }
}


