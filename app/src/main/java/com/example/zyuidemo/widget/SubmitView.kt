package com.example.zyuidemo.widget

import android.animation.ValueAnimator
import android.animation.ValueAnimator.ofFloat
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import com.blankj.utilcode.util.SizeUtils
import com.example.libcommon.utils.ColorUtils.getColor
import com.example.libcommon.utils.getColor
import com.example.zyuidemo.R.color.*
import com.example.zyuidemo.R.styleable
import com.example.zyuidemo.R.styleable.*
import kotlin.math.max


/**
 * zhangyong
 */
class SubmitView : View {

    constructor(context: Context?) : super(context) {
        initView(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    companion object {
        const val STATUS_STROKE = 1
        const val STATUS_COMPLETE = 2
        const val MODE_STROKE_PARTED = 10
        const val MODE_STROKE_AROUND = 11
    }

    private lateinit var pathMeasureStrokeTop: PathMeasure
    private lateinit var pathMeasureStrokeBtm: PathMeasure
    private lateinit var pathMeasureXL: PathMeasure
    private lateinit var pathMeasureXR: PathMeasure
    private lateinit var mPathMeasureTick: PathMeasure
    private lateinit var tickAnim: ValueAnimator
    private lateinit var progressAnim: ValueAnimator

    //view的宽度
    private var mW = 0f

    //view的高度
    private var mH = 0f
    private var isSuccess = false
    private var strokeWidth = -1f
    private var tickWidth = -1f
    private var bacColorStart = 0
    private var bacColorError = 0
    private var bacColorSuccess = 0
    private var strokeColor = 0
    private var tickColor = white.getColor()
    private var textColor = 0
    private var textSize = dp2px(12f)
    private var textY = 0f
    private var textX = 0f
    private var text = "Start"
    private var textLoading = "Loading"

    private var strokeMode = MODE_STROKE_AROUND

    //记录当前在绘制什么
    private var status = STATUS_STROKE
    private var pathMeasurePath = Path()
    private var paint = Paint()

    //手动设置进度
    private var progress = 0f
    private var lastProgress = 0f

    //边框的路径长度
    private var pathMeasureStrokeLen = 0f

    //边框的路径长度当前比率
    private var pathMeasureStrokeLenRatio = 0f
    private var pathMeasureXLenRatio = 0f

    //对钩的路径长度
    private var pathMeasureTickLen = 0f

    //X的路径长度
    private var pathMeasureXLen = 0f

    //默认动画执行时间(毫秒)
    private var duration = 1500L

    //边框动画执行进度
    private var strokeRatio = 0f

    //背景动画执行进度
    private var tickAndXRatio = 0f

    //边框的一半
    private var padding = 0f

    //线的X起始位置
    private var lineStartX = 0f

    //线的X结束位置
    private var lineStopX = 0f

    //线的y
    private var lineTopY = 0f
    private var lineBtmY = 0f

    //右边弧的起始度数
    private val arcRtStartAng = -90f

    //左边弧的起始度数
    private val arcLtStartAng = 90f

    //弧的最大度数
    private val arcSweepAng = 180f

    //复用弧的rect
    private var arcRectF = RectF()

    //对钩的中心点
    private var tickCenX = 0f
    private var tickCenY = 0f

    //对钩左边和右边的比例
    private var tickLenScale = 2.5f

    //对钩左边的长度
    private var tickLtLen = 0f

    //对钩右边的长度
    private var tickRtLen = 0f

    //对钩左边的x点
    private var tickLtStartX = 0f

    //对钩左边的y点
    private var tickLtStartY = 0f

    //对钩右边的x点
    private var tickRtEdX = 0f

    //对钩右边的y点
    private var tickRtEdY = 0f

    @SuppressLint("Recycle")
    private fun initView(context: Context?, attrs: AttributeSet?) {
        context?.run {
            val type = obtainStyledAttributes(attrs, styleable.SubmitView)
            text = type.getString(SubmitView_svText).toString()
            textLoading = type.getString(SubmitView_svTextLoading).toString()
            textColor = type.getColor(SubmitView_svTextColor, black.getColor())
            textSize = type.getDimension(SubmitView_svTextSize, dp2px(12f))
            strokeWidth = type.getDimension(SubmitView_svStrokeWidth, dp2px(2f))
            strokeColor = type.getColor(SubmitView_svStrokeColor, colorSlGray.getColor())
            bacColorStart = type.getColor(SubmitView_svBacStartColor, colorSlGrayLight.getColor())
            bacColorSuccess = type.getColor(SubmitView_svBacSuccessColor, colorSlBrand.getColor())
            bacColorError = type.getColor(SubmitView_svBacErrorColor, colorSlRed.getColor())
            strokeMode = type.getColor(SubmitView_svStrokeMode, MODE_STROKE_AROUND)
            duration = type.getDimension(SubmitView_svDuration, 1500f).toLong()
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (measuredWidth < measuredHeight) {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mW = w.toFloat()
        mH = h.toFloat()
        if (tickWidth == -1f) {
            tickWidth = mH / 20f
        }
        if (strokeWidth == -1f) {
            strokeWidth = SizeUtils.dp2px(2f).toFloat()
        }
        tickRtLen = mH * 1.6f / 3f
        tickLtLen = tickRtLen / tickLenScale
        padding = strokeWidth / 2f
        lineStartX = mH / 2 + padding
        lineStopX = mW - mH / 2 - padding
        lineTopY = padding
        lineBtmY = mH - padding
        tickCenX = mW * 4.5f / 10f
        tickCenY = mH * 2f / 3f
        tickLtStartX = tickCenX - tickLtLen
        tickLtStartY = tickCenY - tickLtLen
        tickRtEdX = tickCenX + tickRtLen
        tickRtEdY = tickCenY - tickRtLen

        //边框路径
        val path = Path()
        path.moveTo(lineStartX, lineTopY)
        path.lineTo(lineStopX, lineTopY)
        arcRectF.left = mW - mH + padding
        arcRectF.top = padding
        arcRectF.right = mW - padding
        arcRectF.bottom = mH - padding
        path.arcTo(arcRectF, arcRtStartAng, arcSweepAng)
        pathMeasureStrokeTop = PathMeasure(path, false)
        pathMeasureStrokeLen = pathMeasureStrokeTop.length
        path.reset()
        path.moveTo(lineStopX, lineBtmY)
        path.lineTo(lineStartX, lineBtmY)

        arcRectF.left = padding
        arcRectF.top = padding
        arcRectF.right = mH + padding
        arcRectF.bottom = mH - padding

        path.arcTo(arcRectF, arcLtStartAng, arcSweepAng)
        pathMeasureStrokeBtm = PathMeasure(path, false)

        //对钩的路径
        path.reset()
        path.moveTo(tickLtStartX, tickLtStartY)
        path.quadTo(
            (tickLtStartX + tickCenX) / 2f + tickLtLen / 10f,
            (tickLtStartY + tickCenY) / 2f - tickLtLen / 10f,
            tickCenX - tickLtLen / 10f,
            tickCenY - tickLtLen / 10f
        )
        path.quadTo(tickCenX, tickCenY, tickCenX + tickLtLen / 10f, tickCenY - tickLtLen / 10f)
        path.quadTo(
            (tickCenX + tickRtEdX) / 2f - tickLtLen / 10f,
            (tickCenY + tickRtEdY) / 2f - tickLtLen / 10f,
            tickRtEdX,
            tickRtEdY
        )
        mPathMeasureTick = PathMeasure(path, false)
        pathMeasureTickLen = mPathMeasureTick.length

        // X的路径
        path.reset()
        val whDiff = (mW - mH) / 2f
        path.moveTo(mH / 2f - mH / 4f + whDiff, mH / 2f - mH / 4f)
        path.lineTo(mH / 2f + mH / 4f + whDiff, mH / 2f + mH / 4f)
        pathMeasureXL = PathMeasure(path, false)
        pathMeasureXLen = pathMeasureXL.length
        path.reset()
        path.moveTo(mH / 2f + mH / 4f + whDiff, mH / 2f - mH / 4f)
        path.lineTo(mH / 2f - mH / 4f + whDiff, mH / 2f + mH / 4f)
        pathMeasureXR = PathMeasure(path, false)

        initPaint()

        textX = mW / 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        if (status == STATUS_COMPLETE) {
            drawComplete(canvas)
        } else if (status == STATUS_STROKE) {
            drawStroke(canvas)
        }
        drawText(canvas)
    }

    private fun drawText(canvas: Canvas) {
        getTextPaint()
        val fontMetrics: Paint.FontMetrics = paint.fontMetrics
        textY = mH / 2f - (fontMetrics.ascent + fontMetrics.descent) / 2f
        paint.color = getColor(textColor, 1f - strokeRatio)
        canvas.drawText(if (strokeRatio == 0f) text else textLoading, textX, textY, paint)
    }

    private fun drawStroke(canvas: Canvas) {
        getStrokePaint()
        pathMeasurePath.reset()
        pathMeasureStrokeLenRatio = pathMeasureStrokeLen * strokeRatio
        if (strokeMode == MODE_STROKE_AROUND) {
            pathMeasureStrokeLenRatio *= 2f
        }
        pathMeasureStrokeTop.getSegment(
            pathMeasureStrokeLenRatio,
            pathMeasureStrokeLen,
            pathMeasurePath,
            true
        )
        canvas.drawPath(pathMeasurePath, paint)
        if (strokeMode == MODE_STROKE_AROUND) {
            pathMeasureStrokeLenRatio = pathMeasureStrokeLen * max((strokeRatio - 0.5f), 0f) * 2f
        }
        pathMeasurePath.reset()
        pathMeasureStrokeBtm.getSegment(
            pathMeasureStrokeLenRatio,
            pathMeasureStrokeLen,
            pathMeasurePath,
            true
        )
        canvas.drawPath(pathMeasurePath, paint)
    }

    private fun drawComplete(canvas: Canvas) {
        getTickAndXPaint()
        pathMeasurePath.reset()
        if (isSuccess) {
            mPathMeasureTick.getSegment(
                0f,
                pathMeasureTickLen * tickAndXRatio,
                pathMeasurePath,
                true
            )
            canvas.drawPath(pathMeasurePath, paint)
            return
        }
        pathMeasureXLenRatio = pathMeasureXLen * tickAndXRatio
        pathMeasureXL.getSegment(0f, pathMeasureXLenRatio, pathMeasurePath, true)
        canvas.drawPath(pathMeasurePath, paint)
        pathMeasurePath.reset()
        pathMeasureXLenRatio = pathMeasureXLen * max(tickAndXRatio - 0.5f, 0f) * 2f
        pathMeasureXR.getSegment(0f, pathMeasureXLenRatio, pathMeasurePath, true)
        canvas.drawPath(pathMeasurePath, paint)
    }

    private fun drawBackground(canvas: Canvas) {
        getBacPaint()
        if (isSuccess) {
            paint.color =
                getColor(bacColorStart, bacColorSuccess, (strokeRatio + tickAndXRatio) / 2f)
        } else {
            paint.color =
                getColor(bacColorStart, bacColorError, (strokeRatio + tickAndXRatio) / 2f)
        }
        canvas.drawRoundRect(0f, 0f, mW, mH, 200f, 200f, paint)
    }

    private fun initPaint() {
        paint.isAntiAlias = true // 设置画笔为抗锯齿
        paint.isDither = true
        paint.strokeCap = Paint.Cap.ROUND
    }

    private fun getStrokePaint() {
        paint.strokeWidth = strokeWidth // 画笔宽度
        paint.style = Paint.Style.STROKE
        paint.color = strokeColor
    }

    private fun getBacPaint() {
        paint.strokeWidth = strokeWidth // 画笔宽度
        paint.style = Paint.Style.FILL
        paint.color = bacColorStart
    }

    private fun getTickAndXPaint() {
        paint.strokeWidth = tickWidth // 画笔宽度
        paint.style = Paint.Style.STROKE
        paint.color = tickColor
    }

    private fun getTextPaint() {
        paint.style = Paint.Style.FILL
        paint.strokeWidth = strokeWidth
        paint.textSize = textSize
        paint.textAlign = Paint.Align.CENTER
    }

    private fun getAnim() {
        if (!::tickAnim.isInitialized) {
            tickAnim = ofFloat(progress, 2f)
            tickAnim.duration = duration
            tickAnim.interpolator = DecelerateInterpolator()
            tickAnim.addUpdateListener { value_animator ->
                val progress = value_animator.animatedValue as Float
                handleProgress(progress)
            }
        }
    }

    private fun handleProgress(value: Float) {
        if (value <= 1f) {
            strokeRatio = value
            tickAndXRatio = 0f
            status = STATUS_STROKE
        } else {
            strokeRatio = 1f
            tickAndXRatio = value - strokeRatio
            status = STATUS_COMPLETE
        }
        postInvalidate()
    }

    fun startErrorAnim() {
        isSuccess = false
        start()
    }

    fun startSuccessAnim() {
        isSuccess = true
        start()
    }

    /**
     * 重置
     */
    fun reset() {
        progress = 0f
        tickAndXRatio = 0f
        strokeRatio = 0f
        status = STATUS_STROKE
        postInvalidate()
    }

    fun setProgress(progress: Float) {
        if (lastProgress > progress) {
            lastProgress = 0f
        }
        this.progress = progress
        if (!::progressAnim.isInitialized) {
            progressAnim = ofFloat(lastProgress, progress)
            progressAnim.duration = duration
            progressAnim.interpolator = LinearInterpolator()
            progressAnim.addUpdateListener { value_animator ->
                val pro = value_animator.animatedValue as Float
                this.lastProgress = pro
                handleProgress(pro)
            }
        }
        progressAnim.cancel()
        progressAnim.setFloatValues(lastProgress, progress)
        progressAnim.start()
    }

    fun complete() {
        startAnim()
    }

    /**
     * 开始动画
     */
    private fun start() {
        progress = 0f
        tickAndXRatio = 0f
        strokeRatio = 0f
        startAnim()
    }

    private fun startAnim() {
        getAnim()
        tickAnim.start()
    }

    /**
     * 设置动画执行时间 毫秒
     * @param duration
     */
    fun setDuration(duration: Long): SubmitView {
        this.duration = duration
        return this
    }

    /**
     * 设置边框宽度
     * @param strokeWidth
     */
    fun setStrokeWidth(strokeWidth: Float): SubmitView {
        this.strokeWidth = strokeWidth
        return this
    }

    /**
     * 设置对钩的宽度
     * @param tickWidth
     */
    fun setTickWidth(tickWidth: Float): SubmitView {
        this.tickWidth = tickWidth
        return this
    }

    /**
     * 设置背景渐变起始颜色
     * @param bacColorStart
     */
    fun setBacStartColor(bacColorStart: Int): SubmitView {
        this.bacColorStart = bacColorStart
        return this
    }

    /**
     * 设置背景渐变结束颜色
     * @param bacColorStart
     */
    fun setBacEndColor(bacColorStart: Int): SubmitView {
        this.bacColorSuccess = bacColorStart
        return this
    }

    /**
     * 设置边框颜色
     * @param strokeColor
     */
    fun setStrokeColor(strokeColor: Int): SubmitView {
        this.strokeColor = strokeColor
        return this
    }

    /**
     * 设置对钩颜色
     * @param tickColor
     */
    fun setTickColor(tickColor: Int): SubmitView {
        this.tickColor = tickColor
        return this
    }

    /**
     * 设置边框的动画模式
     * @param strokeMode [MODE_STROKE_AROUND],[MODE_STROKE_PARTED]
     */
    fun setStrokeMode(strokeMode: Int): SubmitView {
        this.strokeMode = strokeMode
        return this
    }

    private fun dp2px(dpValue: Float): Float {
        val scale = Resources.getSystem().displayMetrics.density
        return (dpValue * scale + 0.5f)
    }

}