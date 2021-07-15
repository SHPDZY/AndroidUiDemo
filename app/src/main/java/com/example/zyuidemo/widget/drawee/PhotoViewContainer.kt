package com.example.zyuidemo.widget.drawee

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import androidx.viewpager2.widget.ViewPager2
import com.lxj.xpopup.interfaces.OnDragChangeListener

/**
 * 拖拽view
 */
class PhotoViewContainer @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private var dragHelper: ViewDragHelper? = null
    var viewPager: ViewPager2? = null
    private var hideTopThreshold = 150
    private var maxOffset = 0
    private var dragChangeListener: OnDragChangeListener? = null
    var isReleasing = false
    private fun init() {
        hideTopThreshold = dip2px(hideTopThreshold.toFloat())
        dragHelper = ViewDragHelper.create(this, cb)
        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        viewPager = getChildAt(0) as? ViewPager2
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        maxOffset = height / 3
    }

    var isVertical = false
    private var touchX = 0f
    private var touchY = 0f
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.pointerCount > 1) return super.dispatchTouchEvent(ev)
        try {
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    touchX = ev.x
                    touchY = ev.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = ev.x - touchX
                    val dy = ev.y - touchY
                    viewPager!!.dispatchTouchEvent(ev)
                    isVertical = Math.abs(dy) > Math.abs(dx)
                    touchX = ev.x
                    touchY = ev.y
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    touchX = 0f
                    touchY = 0f
                    isVertical = false
                }
            }
        } catch (e: Exception) {
        }
        return super.dispatchTouchEvent(ev)
    }

    private val isTopOrBottomEnd: Boolean
        get() {
            viewPager?.run {
                val draweeView = findViewWithTag<View>(currentItem) as? PhotoDraweeView
                    ?: return false
                val attacher = draweeView.attacher
                return attacher.isTop || attacher.isBottom || !attacher.isOutsideOfY
            }
            return false
        }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val result = dragHelper!!.shouldInterceptTouchEvent(ev)
        if (ev.pointerCount > 1 && ev.action == MotionEvent.ACTION_MOVE) return false
        if (!isTopOrBottomEnd) return isTopOrBottomEnd
        return if (isTopOrBottomEnd && isVertical) true else result && isVertical
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (ev.pointerCount > 1) return false
        try {
            dragHelper!!.processTouchEvent(ev)
            return true
        } catch (e: Exception) {
        }
        return true
    }

    var cb: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {
        override fun tryCaptureView(view: View, i: Int): Boolean {
            return !isReleasing
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return 1
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return left
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            val t = viewPager!!.top + dy / 2
            return if (t >= 0) {
                Math.min(t, maxOffset)
            } else {
                -Math.min(-t, maxOffset)
            }
        }

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            if (changedView !== viewPager) {
                viewPager!!.offsetTopAndBottom(dy)
                viewPager!!.offsetLeftAndRight(dx)
            }
            val fraction = Math.abs(top) * 1f / maxOffset
            val pageScale = 1 - fraction * .8f
            viewPager!!.scaleX = pageScale
            viewPager!!.scaleY = pageScale
            changedView.scaleX = pageScale
            changedView.scaleY = pageScale
            //top>0 上滑，上滑时不需要回调
            if (dragChangeListener != null && top >= 0) {
                dragChangeListener!!.onDragChange(dy, pageScale, fraction)
            }
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)
            if (releasedChild.top > hideTopThreshold) {
                if (dragChangeListener != null) dragChangeListener!!.onRelease()
            } else {
                dragHelper!!.smoothSlideViewTo(viewPager!!, 0, 0)
                dragHelper!!.smoothSlideViewTo(releasedChild, 0, 0)
                ViewCompat.postInvalidateOnAnimation(this@PhotoViewContainer)
            }
        }
    }

    override fun computeScroll() {
        super.computeScroll()
        if (dragHelper!!.continueSettling(false)) {
            ViewCompat.postInvalidateOnAnimation(this@PhotoViewContainer)
        }
    }

    fun dip2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun setOnDragChangeListener(listener: OnDragChangeListener?) {
        dragChangeListener = listener
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        isReleasing = false
    }

    init {
        init()
    }
}