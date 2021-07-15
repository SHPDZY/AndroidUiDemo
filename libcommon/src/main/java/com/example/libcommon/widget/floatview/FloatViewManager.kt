package com.example.libcommon.widget.floatview

import android.app.Activity
import android.widget.FrameLayout
import android.widget.ImageView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils.dp2px
import com.blankj.utilcode.util.Utils
import com.example.libcommon.R
import com.example.libcommon.base.activitylifecycle.BaseActivityLifecycleCallbacks

/**
 * @author  : zhangyong
 * @date    : 2021/7/15
 * @desc    :
 * @version :
 */
object FloatViewManager : BaseActivityLifecycleCallbacks(), FloatViewListener {

    private lateinit var layoutParams: FrameLayout.LayoutParams
    private var floatView: FloatView? = null
    private var listener: FloatViewListener? = null


    fun init(): FloatViewManager {
        floatView = FloatView(Utils.getApp())
        val imageView = ImageView(Utils.getApp())
        imageView.setImageResource(R.mipmap.ic_launcher)
        floatView?.addView(imageView)
        layoutParams = FrameLayout.LayoutParams(dp2px(50f), dp2px(50f))
        layoutParams.topMargin = ScreenUtils.getAppScreenHeight() / 3 * 2
        floatView?.setFloatViewListener(this)
        Utils.getApp().registerActivityLifecycleCallbacks(this)
        return this
    }

    fun setClick(listener: FloatViewListener): FloatViewManager {
        this.listener = listener
        return this
    }

    override fun onRemove(floatView: FloatView?) {
        listener?.onRemove(floatView)
    }

    override fun onClick(floatView: FloatView?) {
        listener?.onClick(floatView)
    }

    override fun onActivityResumed(activity: Activity) {
        attach(activity)
    }

    override fun onActivityStopped(activity: Activity) {
        detach(activity)
    }

    private fun attach(activity: Activity) {
        if (floatView == null) return
        val parent = floatView?.parent
        val activityRoot = getActivityRoot(activity)
        if (parent != null && activityRoot == parent) {
            return
        }
        (parent as? FrameLayout)?.removeView(floatView)
        activityRoot?.addView(floatView, layoutParams)
    }

    private fun detach(activity: Activity) {
        if (floatView == null) return
        val parent = floatView?.parent
        val activityRoot = getActivityRoot(activity)
        if (floatView != null && activityRoot == parent) {
            activityRoot?.removeView(floatView)
        }
    }

    private fun getActivityRoot(activity: Activity?): FrameLayout? {
        try {
            return activity?.window?.decorView?.findViewById(android.R.id.content) as? FrameLayout
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}