package com.example.libcommon.utils

import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.DebouncingUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils


/**
 * Created by zhuliyuan on 2018/7/3.
 */

/**
 * 获取View宽高
 */
inline fun View.getViewWH(crossinline block: View.(width: Int, height: Int) -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(
        object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                block(this@getViewWH, width, height)
            }
        })
}


fun View?.gone() {
    this?.visibility = View.GONE
}

fun View?.visible() {
    this?.visibility = View.VISIBLE
}

fun View?.invisible() {
    this?.visibility = View.INVISIBLE
}

inline var View?.isVisible: Boolean
    get() = this?.visibility == View.VISIBLE
    set(value) {
        this?.visibility = if (value) View.VISIBLE else View.GONE
    }


inline var View?.isVisibleInt: Int?
    get() = if (this?.visibility == View.VISIBLE) 1 else 0
    set(value) {
        this?.visibility = if (value ?: 0 > 0) View.VISIBLE else View.GONE
    }

inline var View?.isInvisible: Boolean
    get() = this?.visibility == View.INVISIBLE
    set(value) {
        this?.visibility = if (value) View.INVISIBLE else View.VISIBLE
    }

inline var View?.isGone: Boolean
    get() = this?.visibility == View.GONE
    set(value) {
        this?.visibility = if (value) View.GONE else View.VISIBLE
    }

fun View?.setGone() {
    this?.visibility = View.GONE
}

fun View?.setVisible() {
    this?.visibility = View.VISIBLE
}

fun View?.setInVisible() {
    this?.visibility = View.INVISIBLE
}

fun dp2px(value: Float): Int {
    return SizeUtils.dp2px(value)
}

fun px2dp(value: Float): Int {
    return SizeUtils.px2dp(value)
}

fun toast(msg: String) {
    ToastUtils.showShort(msg)
}

fun String?.isValid(): Boolean {
    return this != null && DebouncingUtils.isValid(this, 1000L)
}

fun String?.isValid(duration: Long): Boolean {
    return this != null && DebouncingUtils.isValid(this, duration)
}

fun View?.isValid(): Boolean {
    return this != null && DebouncingUtils.isValid(this)
}

fun View?.isValid(duration: Long): Boolean {
    return this != null && DebouncingUtils.isValid(this, duration)
}

/**
 * 防止多次点击click listener
 */
inline fun View.setOnAvoidMultipleClickListener(crossinline listener: (View) -> Unit) {
    setOnClickListener {
        if (DebouncingUtils.isValid(this)) {
            listener.invoke(this)
        }
    }
}

inline fun View.setOnAvoidMultipleClickListener(
    crossinline listener: (View) -> Unit,
    duration: Long
) {
    setOnClickListener {
        if (DebouncingUtils.isValid(this, duration)) {
            listener.invoke(this)
        }
    }
}

fun View.setOnAvoidMultipleClickListener(listener: OnAvoidMultipleClickListener?) {
    setOnClickListener {
        if (DebouncingUtils.isValid(this)) {
            listener?.avoidMultipleClickListener(it)
        }
    }
}

fun View.setOnAvoidMultipleClickListener(listener: OnAvoidMultipleClickListener?, duration: Long) {
    setOnClickListener {
        if (DebouncingUtils.isValid(this, duration)) {
            listener?.avoidMultipleClickListener(it)
        }
    }
}

interface OnAvoidMultipleClickListener {
    fun avoidMultipleClickListener(v: View)
}

fun RecyclerView?.removeItemDecorationAtV2(index: Int) {
    this?.run {
        if (itemDecorationCount == 0 || index >= itemDecorationCount) return@run
        removeItemDecorationAt(index)
    }
}