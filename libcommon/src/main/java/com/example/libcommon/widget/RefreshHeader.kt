package com.example.libcommon.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.libcommon.R
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle


/**
 * @author: yutao
 * @date: 2021/3/17
 * @desc:
 */
class RefreshHeader : LinearLayout,
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

    //private var mImage: ImageView? = null
    private var tvNotice: TextView? = null


    private fun initView(context: Context) {
        val view = inflate(context, R.layout.sl_refresh_header, this)
        //mImage = view.findViewById<View>(R.id.hot_iv_refresh_header) as ImageView
        tvNotice = view.findViewById(R.id.tv_show_notice)
    }


    var refreshListener: RefreshStateListener? = null
    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        Log.e(TAG, "onStateChanged: $newState")
        refreshListener?.run {
            onStateChanged(newState)
        }
//        mImage?.run {
//            if(animation!=null){
//                if(animation.hasStarted())
//                    return
//                mImage?.startAnimation(AnimationUtils.loadAnimation(context, R.anim.loading_rotate_anim))
//            }else{
//                mImage?.startAnimation(AnimationUtils.loadAnimation(context, R.anim.loading_rotate_anim))
//            }
//        }
    }

    override fun getView(): View {
        return this
    }

    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate
    }

    override fun setPrimaryColors(vararg colors: Int) {

    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {

    }

    override fun onMoving(
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        height: Int,
        maxDragHeight: Int
    ) {

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

    interface RefreshStateListener {
        fun onStateChanged(state: RefreshState) {}
    }

}