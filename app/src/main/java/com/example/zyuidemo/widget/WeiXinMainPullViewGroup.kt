package com.example.zyuidemo.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import com.example.zyuidemo.R
import com.example.zyuidemo.ui.fragment.WeChatFragment
import java.math.BigDecimal

class WeiXinMainPullViewGroup @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    public var viewDragHelper: ViewDragHelper = ViewDragHelper.create(this, 0.5f, DragHandler());

    var headerMaskView: WeiXinPullHeaderMaskView? = null

    var isOpen: Boolean = false;

    val NAVIGAATION_HEIGHT = 100

    init {

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        for (index in 0 until childCount) {
            if (getChildAt(index) != headerMaskView) {
                getChildAt(index).layout(l, paddingTop, r, b)
            }
        }

    }

    override fun computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }

    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        Log.i("TAG", "onInterceptTouchEvent: ${ev.action}")
        MotionEvent.ACTION_MOVE
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        viewDragHelper.processTouchEvent(event)
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)
    }

    fun createMaskView() {
        if (headerMaskView == null) {
            headerMaskView = WeiXinPullHeaderMaskView(context, null, 0)
            addView(headerMaskView)
        }
    }

    inner class DragHandler : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child.id == R.id.layout_main
        }

        override fun onViewDragStateChanged(state: Int) {
            super.onViewDragStateChanged(state)
        }

        /**
         * 设置进度，设置遮罩layout
         */
        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            createMaskView();
            var programView = getChildAt(0)

            var divide = BigDecimal(top.toString()).divide(
                BigDecimal(measuredHeight - NAVIGAATION_HEIGHT),
                4,
                BigDecimal.ROUND_HALF_UP
            )
            divide = divide.multiply(BigDecimal("100"))

            divide = divide.multiply(BigDecimal("0.002"))
            divide = divide.add(BigDecimal("0.8"))

            if (!isOpen) {
                programView.scaleX = divide.toFloat()
                programView.scaleY = divide.toFloat()
            } else {
                programView.top = paddingTop + (-((measuredHeight - NAVIGAATION_HEIGHT) - top))
            }

            headerMaskView!!.maxHeight = measuredHeight / 3
            headerMaskView!!.layout(0, paddingTop, measuredWidth, top)
            headerMaskView!!.setProgress(
                top.toFloat() / ((measuredHeight - (NAVIGAATION_HEIGHT + paddingTop)) / 3) * 100,
                measuredHeight - (NAVIGAATION_HEIGHT + paddingTop)
            )
            if (top == paddingTop) {
                isOpen = false
            }
            if (top == measuredHeight - NAVIGAATION_HEIGHT) {
                isOpen = true
            }

        }

        override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
            super.onViewCaptured(capturedChild, activePointerId)
            var programView = getChildAt(0)
            programView.top = paddingTop;
        }

        /**
         * 释放
         */
        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {

            /**
             * 如果已经打开或者释放后小于屏幕三分之一，回到原位
             */
            if (isOpen or (releasedChild.top + paddingTop <= measuredHeight / 3)) {
                viewDragHelper.smoothSlideViewTo(releasedChild, 0, paddingTop);
                ViewCompat.postInvalidateOnAnimation(this@WeiXinMainPullViewGroup);
                return
            }
            viewDragHelper.smoothSlideViewTo(releasedChild, 0, measuredHeight - NAVIGAATION_HEIGHT);
            ViewCompat.postInvalidateOnAnimation(this@WeiXinMainPullViewGroup);
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            if (top <= paddingTop) {
                return paddingTop
            }
            return (child.top + dy / 1.3).toInt();
        }

    }
}

class WeiXinPullHeaderMaskView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet?,
    defStyleAttr: Int
) :
    View(context, attrs, defStyleAttr) {

    var isVibrator: Boolean = false;
    var progress: Int = 0;
    var maxHeight: Int = 0;
    private val CIRCLE_MAX_SIZE = 32;
    var parentHeight=0;
    var paint = Paint()
    private val DEFAULT_CIRCLE_SIZE=8f;
    init {
        setBackgroundColor(Color.argb(255 , 239, 239, 239))
        paint.alpha=255;
        paint.color = ContextCompat.getColor(context!!, R.color.colorTabIndicatorColor)
        paint.isAntiAlias = true;
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var value = height.toFloat() / maxHeight

        if (height <= maxHeight / 2) {
            canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), CIRCLE_MAX_SIZE * value, paint)
        } else {
            if (progress<100){
                var diff = (value - 0.5f) * CIRCLE_MAX_SIZE
                canvas.drawCircle(((width / 2).toFloat()-((0.4f-value)*100)), (height / 2).toFloat(), DEFAULT_CIRCLE_SIZE, paint)
                canvas.drawCircle(((width / 2).toFloat()+((0.4f-value)*100)), (height / 2).toFloat(), DEFAULT_CIRCLE_SIZE, paint)
                if ((CIRCLE_MAX_SIZE * 0.5f) - diff<=DEFAULT_CIRCLE_SIZE){
                    canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), DEFAULT_CIRCLE_SIZE, paint)
                }else{
                    canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (CIRCLE_MAX_SIZE * 0.5f) - diff, paint)
                }

            }else{
                paint.alpha=getAlphaValue();
                canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), DEFAULT_CIRCLE_SIZE, paint)
                canvas.drawCircle((width / 2).toFloat()-((0.4f)*100), (height / 2).toFloat(), DEFAULT_CIRCLE_SIZE, paint)
                canvas.drawCircle((width / 2).toFloat()+(((0.4f)*100)), (height / 2).toFloat(), DEFAULT_CIRCLE_SIZE, paint)
            }
        }

    }
    private fun getAlphaValue():Int{
        val dc=parentHeight/3-ViewUtils.getStatusBarHeight(resources);
        val alpha=((height).toFloat()-dc)/(parentHeight-(dc))
        return 255-(255*alpha).toInt()
    }

    private fun vibrator() {
        var vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            var createOneShot = VibrationEffect.createOneShot(7, 255)
            vibrator.vibrate(createOneShot)
        } else {
            vibrator.vibrate(7)
        }
    }

    fun setProgress(value: Float,parentHeight:Int) {
        this.progress = value.toInt();
        this.parentHeight=parentHeight;
        if (value >= 100 && !isVibrator) {
            vibrator()
            isVibrator = true;
        }
        if (value < 100) {
            isVibrator = false;
        }
        if (progress>=100){
            setBackgroundColor(Color.argb(getAlphaValue() , 239, 239, 239))
            var mainActivity = context as? WeChatFragment ?:return
            mainActivity.changeStatusBackgroundAlphaValue(getAlphaValue())
        }else{
            setBackgroundColor(Color.argb(255, 239, 239, 239))
        }
        invalidate()
    }


}

class ViewUtils {
    companion object{
        @JvmStatic
        fun getStatusBarHeight(resources: Resources): Int {
            var result = 0
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = resources.getDimensionPixelSize(resourceId)
            }
            return result
        }
    }
}
