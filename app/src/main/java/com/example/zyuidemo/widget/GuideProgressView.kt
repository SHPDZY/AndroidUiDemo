package com.example.zyuidemo.widget

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.blankj.utilcode.util.SizeUtils
import kotlin.math.abs


/**
 * zhangyong
 * 体重记录-增重减重进度条
 */
class GuideProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mW: Float = 0f
    private var mH: Float = 0f
    private lateinit var paintProgress: Paint
    private lateinit var paintPath: Paint
    private lateinit var paintBackground: Paint
    private var strokeWidth: Float = SizeUtils.dp2px(2f).toFloat()
    private var progress: Float = 0f //进度
    private var progressBackgroundColor = Color.parseColor("#F5F6FA")
    private var progressColor = Color.GREEN
    private var percent = 0f //动画执行进度
    private var duration = 1000 //默认动画执行时间(毫秒)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mW = w.toFloat()
        mH = h.toFloat()
        initProgressPaint()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val padding = strokeWidth / 2

        //弧的半径
        val arcRadius = mH / 2
        //线的长度
        val lineLength = mW - mH - padding * 2f
        //弧的长度
        val arcGirth = 2f * 3.14f * arcRadius
        //单边总长度
        val totalLength = lineLength + arcGirth
        //线占得比例
        val lineRatio = lineLength / totalLength
        //弧占得比例
        val arcRatio = arcGirth / totalLength
        //当前进度应该减去的长度
        val percentLength = totalLength * percent
        //线的X起始位置
        val lineStartX = mH / 2 + padding
        //线的X结束位置
        val lineStopX = mW - mH / 2 - padding
        //线的X当前进度的起始位置
        val lineStartXPercent = lineStartX + percentLength
        //弧的当前进度
        var arcRatioPercent = 1f
        if (lineStartXPercent < lineStopX) {
            //线起始位置等于结束位置的时候就不画了，小于的时候才画
            canvas.drawLine(lineStartXPercent, padding, lineStopX, padding, paintProgress)
        } else {
            arcRatioPercent = if (percent >= 1f) {
                0f
            } else {
                1f - (percent - lineRatio) / arcRatio
            }
        }
        //弧的起始度数
        val arcStartAngle = -90f
        //弧的最大度数
        val arcSweepAngle = 180f
        //弧的当前度数
        val arcStartAnglePercent = arcStartAngle + (arcSweepAngle - arcSweepAngle * arcRatioPercent)
        //弧的当前最大度数
        val arcSweepAnglePercent = arcSweepAngle * arcRatioPercent
        //画线
        val rectFArc = RectF(mW - mH + padding, padding, mW - padding, mH - padding)
        //画弧
        canvas.drawArc(rectFArc, arcStartAnglePercent, arcSweepAnglePercent, false, paintProgress)
    }

    private fun initProgressPaint() {
        paintProgress = Paint()
        paintProgress.isAntiAlias = true // 设置画笔为抗锯齿
        paintProgress.strokeCap = Paint.Cap.ROUND
        paintProgress.strokeWidth = strokeWidth // 画笔宽度
        paintProgress.style = Paint.Style.STROKE
        paintProgress.color = progressColor
        paintProgress.isDither = true
    }

    private fun initBacPaint() {
        paintBackground = Paint()
        paintBackground.isAntiAlias = true // 设置画笔为抗锯齿
        paintBackground.strokeWidth = strokeWidth // 画笔宽度
        paintBackground.strokeCap = Paint.Cap.ROUND
        paintBackground.style = Paint.Style.FILL
        paintBackground.isDither = true
        paintBackground.color = progressBackgroundColor
    }

    private fun initPathPaint() {
        paintPath = Paint()
        paintPath.isAntiAlias = true // 设置画笔为抗锯齿
        paintPath.strokeWidth = mW // 画笔宽度
    }

    /**
     * 开始动画
     *
     * @param
     */
    fun start() {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = duration.toLong()
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener { value_animator ->
            val value = value_animator.animatedValue as Float
            percent = value
            postInvalidate()
        }
        valueAnimator.start()
    }

    fun setStrokeWidth(strokeWidth: Int) {
        this.strokeWidth = strokeWidth.toFloat()
    }

    fun setProgressBackgroundColor(progressBackgroundColor: Int) {
        this.progressBackgroundColor = progressBackgroundColor
    }

    fun setProgressColor(progressColor: Int) {
        this.progressColor = progressColor
        paintProgress.color = progressColor
    }

    fun setProgress(progress: Float) {
        this.progress = if (progress < -1) 1f else abs(progress)
    }

    /**
     * 设置动画执行时间 毫秒
     *
     * @param duration
     */
    fun setDuration(duration: Int) {
        this.duration = duration
    }
}