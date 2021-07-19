package com.example.zyuidemo.widget

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.blankj.utilcode.util.LogUtils
import com.example.libcommon.utils.getColor
import com.example.zyuidemo.R
import kotlin.math.abs


/**
 * zhangyong
 * 体重增重减重进度条
 */
class WeightProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var paintProgress: Paint
    private lateinit var paintPath: Paint
    private lateinit var paintText: Paint
    private lateinit var paintDot: Paint
    private lateinit var paintBackground: Paint
    private var valueAnimator: ValueAnimator? = null
    private var reserveWidth: Float = dip2pxFloat(29f)
    private var strokeWidth: Float = dip2pxFloat(6f)
    private var dotWidth: Float = dip2pxFloat(2f)
    private var indicatorMargin: Float = dip2pxFloat(4f)
    private var indicatorTextSize: Float = dip2pxFloat(12f)
    private var progress: Float = 0f //进度
    private var progressBackgroundColor = Color.parseColor("#F5F6FA")
    private var progressColor = Color.WHITE
    private var percent = 0f //动画执行进度
    private var duration = 1000 //默认动画执行时间(毫秒)
    private var isError = false //是否进度低于百分之0
    private var isOutOfProgress = false //是否超出百分之100
    private var indicatorBitmapBgError: Bitmap
    private var indicatorBitmapBg: Bitmap
    private var indicatorBitmapWidth: Float = 0f
    private var indicatorBitmapHeight: Float = 0f
    private var indicatorContent: Float = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initBacPaint()
        initTextPaint()
        initDotPaint()
        initProgressPaint()
        initPathPaint()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //计算进度条的Y轴位置
        val progressMarginTop = indicatorBitmapHeight + indicatorMargin + strokeWidth / 2
        //进度条0-100百分比的有效宽度
        val progressRealWidth = width.toFloat() - reserveWidth * 2
        //根据当前进度计算进度条距离起始位置距离
        val progressMarginStart = when {
            //进度低于0
            isError -> reserveWidth - reserveWidth * progress * percent + (if (progress > 0.5) strokeWidth / 2 else 0f)
            //进度大于1
            isOutOfProgress -> progressRealWidth * percent + reserveWidth * (Math.min(
                progress - 1f,
                1f
            )) * percent + reserveWidth - (if (progress > 1.5) strokeWidth / 2 else 0f)
            else -> progressRealWidth * progress * percent + reserveWidth
        }
        LogUtils.d("progressMarginStart:$progressMarginStart")
        //绘制底部背景
        drawBackground(canvas, progressMarginTop)
        //绘制进度
        drawProgress(progressMarginStart, canvas, progressMarginTop)
        //绘制指示器背景和文字
        drawIndicatorBgAndText(progressMarginStart, canvas)
        //绘制进度起点和终点的小圆点
        drawDot(canvas, progressMarginTop)
    }

    private fun drawBackground(canvas: Canvas, progressMarginTop: Float) {
        canvas.drawLine(
            strokeWidth / 2,
            progressMarginTop,
            width.toFloat() - strokeWidth / 2,
            progressMarginTop,
            paintBackground
        )
    }

    private fun drawProgress(progressMarginStart: Float, canvas: Canvas, progressMarginTop: Float) {
        paintProgress.shader = if (isError) LinearGradient(
            reserveWidth, 0f, progressMarginStart, 0f,
            Color.parseColor("#FFAEAA"),
            Color.parseColor("#FF6C65"), Shader.TileMode.CLAMP
        ) else LinearGradient(
            reserveWidth, 0f, progressMarginStart, 0f,
            Color.parseColor("#9DE6DF"),
            Color.parseColor("#00C4B3"), Shader.TileMode.CLAMP
        )
        canvas.drawLine(
            reserveWidth,
            progressMarginTop,
            progressMarginStart,
            progressMarginTop,
            paintProgress
        )
    }

    private fun drawIndicatorBgAndText(progressMarginStart: Float, canvas: Canvas) {
        var indicatorRealMarginStart = progressMarginStart
        if (progressMarginStart + indicatorBitmapWidth > width) {
            indicatorRealMarginStart = width.toFloat() - indicatorBitmapWidth
        } else if (progressMarginStart - indicatorBitmapWidth < 0) {
            indicatorRealMarginStart = indicatorBitmapWidth
        }
        canvas.drawBitmap(
            if (isError) indicatorBitmapBgError else indicatorBitmapBg,
            indicatorRealMarginStart - indicatorBitmapWidth,
            0f,
            paintPath
        )
        //绘制指示器的数字
        val fontMetrics: Paint.FontMetrics = paintText.fontMetrics
        val distance = (fontMetrics.bottom - fontMetrics.top) / 2
        val baseline = distance + dip2pxFloat(5f)
        canvas.drawText(
            String.format("%.1f", indicatorContent * percent),
            indicatorRealMarginStart,
            baseline,
            paintText
        )
    }

    private fun drawDot(canvas: Canvas, progressMarginTop: Float) {
        paintDot.color = Color.WHITE
        canvas.drawCircle(reserveWidth, progressMarginTop, dotWidth, paintDot)
        paintDot.color =
            if (isOutOfProgress) Color.WHITE else R.color.colorSlBrand.getColor()
        canvas.drawCircle(width - reserveWidth, progressMarginTop, dotWidth, paintDot)
    }

    private fun initProgressPaint() {
        paintProgress = Paint()
        paintProgress.isAntiAlias = true // 设置画笔为抗锯齿
        paintProgress.strokeWidth = strokeWidth // 画笔宽度
        paintProgress.strokeCap = Paint.Cap.ROUND
        paintProgress.style = Paint.Style.FILL_AND_STROKE
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

    private fun initTextPaint() {
        paintText = Paint()
        paintText.isAntiAlias = true // 设置画笔为抗锯齿
        paintText.strokeWidth = 1f // 画笔宽度
        paintText.style = Paint.Style.FILL
        paintText.textAlign = Paint.Align.CENTER
        paintText.textSize = indicatorTextSize
        paintText.color = Color.WHITE
    }

    private fun initDotPaint() {
        paintDot = Paint()
        paintText.isAntiAlias = true // 设置画笔为抗锯齿
        paintText.strokeWidth = 1f // 画笔宽度
        paintText.style = Paint.Style.FILL
        paintText.color = Color.WHITE
    }

    private fun initPathPaint() {
        paintPath = Paint()
        paintPath.isAntiAlias = true // 设置画笔为抗锯齿
        paintPath.strokeWidth = width.toFloat() // 画笔宽度
    }

    /**
     * 开始动画
     *
     * @param
     */
    fun start() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(0.1f, 1f)
            valueAnimator?.duration = duration.toLong()
            valueAnimator?.interpolator = LinearInterpolator()
            valueAnimator?.addUpdateListener { value_animator ->
                val value = value_animator.animatedValue as Float
                percent = value
                LogUtils.d("ValueAnimator percent:$percent")
                postInvalidate()
            }
        }
        valueAnimator?.start()
    }

    fun pause(){
        valueAnimator?.pause()
    }

    fun setStrokeWidth(strokeWidth: Int) {
        this.strokeWidth = strokeWidth.toFloat()
    }

    fun setLatestWeight(latestWeight: Float) {
        this.indicatorContent = latestWeight
    }

    fun setProgressBackgroundColor(progressBackgroundColor: Int) {
        this.progressBackgroundColor = progressBackgroundColor
    }

    fun setProgressColor(progressColor: Int) {
        this.progressColor = progressColor
        paintProgress.color = progressColor
    }

    fun setProgress(progress: Float) {
        this.isError = progress < 0
        this.progress = if (progress < -1) 1f else abs(progress)
        this.isOutOfProgress = progress >= 1.0f
    }

    /**
     * 设置动画执行时间 毫秒
     *
     * @param duration
     */
    fun setDuration(duration: Int) {
        this.duration = duration
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.WeightProgressView)
        indicatorBitmapBg = BitmapFactory.decodeResource(
            resources, a.getResourceId(
                R.styleable.WeightProgressView_wpvIndicatorBac,
                R.drawable.weight_ic_health_report_pop
            )
        )
        indicatorBitmapBgError = BitmapFactory.decodeResource(
            resources, a.getResourceId(
                R.styleable.WeightProgressView_wpvIndicatorBacError,
                R.drawable.weight_ic_health_report_pop_error
            )
        )
        indicatorTextSize =
            a.getDimension(R.styleable.WeightProgressView_wpvIndicatorTextSize, indicatorTextSize)
        indicatorBitmapWidth = indicatorBitmapBg.width / 2f
        indicatorBitmapHeight = indicatorBitmapBg.height.toFloat()
        progressBackgroundColor = a.getColor(
            R.styleable.WeightProgressView_wpvProgressBackgroundColor,
            progressBackgroundColor
        )
        progressColor = a.getColor(R.styleable.WeightProgressView_wpvProgressColor, progressColor)
        strokeWidth = a.getDimension(R.styleable.WeightProgressView_wpvStrokeWidth, strokeWidth)
        progress = a.getFloat(R.styleable.WeightProgressView_wpvProgress, progress)
        duration = a.getInt(R.styleable.WeightProgressView_wpvDuration, duration)
        a.recycle()
    }

    /**
     * dp to px
     */
    private fun dip2pxFloat(dpValue: Float): Float {
        return if (context != null) {
            val scale = context.resources.displayMetrics.density
            dpValue * scale + 0.5f
        } else {
            dpValue
        }
    }
}