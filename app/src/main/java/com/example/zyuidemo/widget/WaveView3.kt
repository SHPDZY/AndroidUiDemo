package com.example.zyuidemo.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.example.libcommon.utils.dp2px

class WaveView3 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val mPaint: Paint = Paint()
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mWaveHeight: Int = 0
    private val mWaveDx: Int
    private var dx: Int = 0
    private var dy: Float = 0f
    private var currentValue: Float = 0f
    private var changeValue: Float = 0f
    private var valueAnimator: ValueAnimator? = null
    private var oldValue: Float = 0f
    private var yAnimator: ValueAnimator? = null

    init {
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.style = Paint.Style.FILL
        //波长的长度(这里设置为屏幕的宽度)
        mWaveDx = resources.displayMetrics.widthPixels
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measureView(widthMeasureSpec, mWaveDx)
        mHeight = measureView(heightMeasureSpec, 300)
        mWaveHeight = dp2px(10f)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawWave(canvas)
    }


    private fun drawWave(canvas: Canvas) {
        val path = Path()
        path.reset()
        path.moveTo((-mWaveDx + dx).toFloat(), changeValue)
        var i = -mWaveDx
        while (i < width + mWaveDx) {
            path.rQuadTo(
                (mWaveDx / 4).toFloat(),
                (-mWaveHeight).toFloat(),
                (mWaveDx / 2).toFloat(),
                0f
            )
            path.rQuadTo(
                (mWaveDx / 4).toFloat(),
                mWaveHeight.toFloat(),
                (mWaveDx / 2).toFloat(),
                0f
            )
            i += mWaveDx

        }
        path.lineTo(mWidth.toFloat(), mHeight.toFloat())
        path.lineTo(0f, mHeight.toFloat())
        path.close()
        canvas.drawPath(path, mPaint)
    }

    private fun startAnimation() {
        val shader = LinearGradient(
            0f, 0f, 0f, mHeight.toFloat(),
            Color.parseColor("#C7E3FF"),
            Color.parseColor("#F0F7FF"), Shader.TileMode.CLAMP
        )
        mPaint.shader = shader
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofInt(0, mWaveDx)
            valueAnimator?.duration = 2000
            valueAnimator?.repeatCount = ValueAnimator.INFINITE
            valueAnimator?.interpolator = LinearInterpolator()
            valueAnimator?.addUpdateListener { animation ->
                dx = animation.animatedValue as Int
                invalidate()
            }
        }

        yAnimator?.also {
            if (it.isRunning) {
                it.cancel()
            }
        }
        yAnimator = ValueAnimator.ofFloat(oldValue, currentValue)
        yAnimator?.duration = 1500
        yAnimator?.interpolator = LinearInterpolator()
        yAnimator?.addUpdateListener { animation ->
            dy = animation.animatedValue as Float
            changeValue = mHeight - dy
            oldValue = dy
        }
        yAnimator?.start()
        oldValue = currentValue


        valueAnimator?.also {
            if (!it.isRunning) {
                it.start()
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        valueAnimator?.also {
            if (it.isRunning) {
                it.cancel()
                valueAnimator = null
            }
        }
        yAnimator?.also {
            if (it.isRunning) {
                it.cancel()
                yAnimator = null
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (isInEditMode){
            setPercentage(0.5f)
        }
    }

    fun setPercentage(percentage: Float?) {
        percentage ?: return
        post {
            when {
                percentage > 1f -> {
                    currentValue = mHeight.toFloat() - 18
                    visibility = VISIBLE
                    startAnimation()
                }
                percentage <= 0f -> {
                    currentValue = 0f
                    oldValue = 0f
                    visibility = INVISIBLE
                    valueAnimator?.also {
                        if (it.isRunning) {
                            it.cancel()
                        }
                    }
                }
                else -> {
                    currentValue = mHeight * percentage
                    if ((mHeight - currentValue) < 18) {
                        currentValue -= 18
                    }
                    visibility = VISIBLE
                    startAnimation()
                }
            }

        }

    }

    private fun measureView(measureSpec: Int, defaultSize: Int): Int {
        var measureSize: Int
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        if (mode == MeasureSpec.EXACTLY) {
            measureSize = size
        } else {
            measureSize = defaultSize
            if (mode == MeasureSpec.AT_MOST) {
                measureSize = Math.min(measureSize, size)
            }
        }
        return measureSize
    }
}
