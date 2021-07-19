package com.example.libcommon.widget.floatview

import android.app.Activity
import android.os.Handler
import android.view.MotionEvent
import android.view.ViewParent
import android.widget.FrameLayout
import android.widget.ImageView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils.dp2px
import com.blankj.utilcode.util.Utils
import com.example.libcommon.R
import com.example.libcommon.base.activitylifecycle.BaseActivityLifecycleCallbacks
import com.example.libcommon.utils.AnimationUtils

/**
 * @author  : zhangyong
 * @date    : 2021/7/15
 * @desc    :
 * @version :
 */
object FloatViewManager : BaseActivityLifecycleCallbacks(), FloatViewListener {

    private const val TOUCH_TIME_THRESHOLD = 5000L
    private const val TOUCH_ALPHA_NORMAL = 0.4f
    private const val TOUCH_ALPHA_PRESS = 1.0f

    private lateinit var layoutParams: FrameLayout.LayoutParams
    private var floatView: FloatView? = null
    private var listener: FloatViewManagerListener? = null
    private var notDisplayActivities = arrayListOf<Any?>()
    private var toolHandler = Handler(Utils.getApp().mainLooper)

    fun init(): FloatViewManager {
        floatView = FloatView(Utils.getApp())
        val imageView = ImageView(Utils.getApp())
        imageView.setImageResource(R.drawable.ic_tools)
        floatView?.addView(imageView)
        layoutParams = FrameLayout.LayoutParams(dp2px(50f), dp2px(50f))
        layoutParams.topMargin = ScreenUtils.getAppScreenHeight() / 3 * 2
        floatView?.setFloatViewListener(this)
        floatView?.setPadding(10, 10, 10, 10)
        floatView?.alpha = TOUCH_ALPHA_NORMAL
        Utils.getApp().registerActivityLifecycleCallbacks(this)
        return this
    }

    fun addNotDisplayActivity(vararg name: String): FloatViewManager {
        notDisplayActivities.addAll(name)
        return this
    }

    fun setClickListener(listener: FloatViewManagerListener): FloatViewManager {
        this.listener = listener
        return this
    }

    override fun onRemove(floatView: FloatView?) {
        listener?.onRemove(floatView)
    }

    override fun onClick(floatView: FloatView?) {
        listener?.onClick(floatView)
    }

    override fun onTouch(event: MotionEvent?) {
        floatView?.run {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    AnimationUtils.alphaView(this, alpha, TOUCH_ALPHA_PRESS)
                    toolHandler.removeCallbacksAndMessages(null)
                }
                MotionEvent.ACTION_UP -> {
                    toolHandler.postDelayed({
                        AnimationUtils.alphaView(this, alpha, TOUCH_ALPHA_NORMAL)
                    }, TOUCH_TIME_THRESHOLD)
                }
                else -> { }
            }
        }
    }

    override fun onActivityResumed(activity: Activity) {
        attach(activity)
    }

    override fun onActivityStopped(activity: Activity) {
        detach(activity)
    }

    private fun attach(activity: Activity?) {
        if (notDisplayActivities.contains(activity?.javaClass?.name)) return
        if (floatView == null) return
        val parent = floatView?.parent
        val activityRoot = getActivityRoot(activity)
        //view的parent和当前activity一致的话不需要addview
        if (parent != null && activityRoot == parent) {
            handleEnable(parent)
            return
        }
        (parent as? FrameLayout)?.removeView(floatView)
        if (!floatIsEnable()) return
        activityRoot?.addView(floatView, layoutParams)
    }

    private fun detach(activity: Activity?) {
        if (floatView == null) return
        val parent = floatView?.parent
        val activityRoot = getActivityRoot(activity)
        if (floatView != null && activityRoot == parent) {
            activityRoot?.removeView(floatView)
        }
    }

    /**
     * isEnable = false 要remove
     */
    private fun handleEnable(parent: ViewParent?) {
        if (!floatIsEnable()) {
            (parent as? FrameLayout)?.removeView(floatView)
        }
    }

    private fun floatIsEnable() = this.listener?.isEnable() == true

    private fun getActivityRoot(activity: Activity?): FrameLayout? {
        try {
            return activity?.window?.decorView?.findViewById(android.R.id.content) as? FrameLayout
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}