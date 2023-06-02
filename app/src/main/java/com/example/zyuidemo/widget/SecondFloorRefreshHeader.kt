package com.example.zyuidemo.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.libcommon.utils.dp2px
import com.example.zyuidemo.R
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import kotlin.math.min


/**
 * @desc:
 */
class SecondFloorRefreshHeader : LinearLayout,
    RefreshHeader {

    private val TAG = javaClass.name

    constructor(context: Context) : this(context, null) {
        initView(context)
    }

    constructor(context: Context, attributeSet: AttributeSet) : this(context, attributeSet, 0) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private var tvNotice: TextView? = null


    private fun initView(context: Context) {
        val view = inflate(context, R.layout.view_second_floor_refresh_header, this)
        tvNotice = view.findViewById(R.id.tv_show_notice)
    }


    var refreshListener: SecondFloorListener? = null
    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        Log.e(TAG, "onStateChanged: $newState")
        this.refreshStateNow = newState
        handleState(-1)
    }

    private var refreshStateNow: RefreshState? = null


    override fun getView(): View {
        return this
    }

    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate
    }

    override fun setPrimaryColors(vararg colors: Int) {

    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
        Log.e(
            TAG, "height=" + height + ",maxDragHeight=" + maxDragHeight
        )
        heightWallpager = maxDragHeight
    }

    override fun onMoving(
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        height: Int,
        maxDragHeight: Int
    ) {
        Log.e(
            TAG,
            "onMoving: isDragging=" + isDragging + ", percent=" + percent + ", offset=" + offset +
                    ", height=" + height + ", maxDragHeight=" + maxDragHeight
        )
        if (isDragging) {
            handleHeaderPulling(offset)
        }
        refreshListener?.onPulling(maxDragHeight, offset)
    }

    private var lastState = 1

    private var heightRefresh: Int = dp2px(80f)
    private var heightAlpha: Float = dp2px(50f).toFloat()
    var heightWallpager: Int = dp2px(150f)

    fun handleHeaderPulling(offset: Int) {
        alpha = min(offset / heightAlpha, 1f)
        when {
            offset in 30..heightRefresh -> {
                groupChange(0, offset)
            }
            offset > heightWallpager -> {
                groupChange(2, offset)
            }
            else -> {
                groupChange(1, offset)
            }
        }
    }

    private fun groupChange(state: Int, offset: Int) {
        lastState = state
        handleState(offset)
    }

    private fun handleState(offset: Int) {
        when (refreshStateNow) {
            RefreshState.PullDownToRefresh -> {
                tvNotice?.text = "下拉刷新…"
            }
            RefreshState.ReleaseToRefresh -> {
                if (lastState == 2) {
                    tvNotice?.text = "下拉查看今日壁纸～"
                } else {
//                    progressBar?.visibility = View.VISIBLE
//                    ivDropDownWall?.visibility = View.GONE
                    tvNotice?.text = "松开刷新…"
                }
            }
            RefreshState.RefreshReleased -> {
                refreshListener?.onSecondFloorOpen()
            }
            RefreshState.Refreshing -> {
                refreshListener?.onSecondFloorRefresh()
            }
            else -> {

            }
        }
    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {

    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
        //mImage?.startAnimation(AnimationUtils.loadAnimation(context, R.anim.loading_rotate_anim))
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        Log.e(TAG, "onFinish: ")
        //mImage?.clearAnimation()
        return 0
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {

    }

    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }

    interface SecondFloorListener {
        fun onRefresh()

        fun onSecondFloorOpen()

        fun onSecondFloorRefresh()

        fun onPulling(offsetStart: Int, offset: Int)
    }

}