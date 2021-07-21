package com.example.zyuidemo.widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.text.TextPaint
import android.util.AttributeSet
import android.util.SparseArray
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SizeUtils
import com.example.libcommon.beans.StatusMarkNote
import com.example.libcommon.beans.StatusMarkNoteQuestion
import com.example.libcommon.utils.isValid
import com.example.libcommon.utils.isVisible
import com.example.libcommon.widget.WidthEqualsHeightButton
import com.example.zyuidemo.R
import com.example.zyuidemo.databinding.ViewStatusMarkLayoutBinding
import kotlin.math.abs


/**
 * ZhangYong
 * 状态打分view
 */
class StatusMarkLayoutView : FrameLayout, View.OnClickListener {

    lateinit var binding: ViewStatusMarkLayoutBinding
    private val btnViews = SparseArray<TextView>()
    private var valueAnimator: ValueAnimator? = null
    private val leftMargin = SizeUtils.dp2px(2f)
    private var oldPos = 0 //上一次点击的位置
    private var newPos = -1 //当前点击的位置
    private var oldOffsetXBg = 0f //气泡背景偏移记录
    private var newOffsetXBg = 0f //气泡背景偏移量
    private var newOffsetXCursor = 0f //气泡背景下的倒三角游标偏移记录
    private var oldOffsetXCursor = 0f //气泡背景下的倒三角游标偏移记录
    private var oldTvStatusNewWidth = 0 //记录气泡背景框的宽度
    private var isRunning = false

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private fun initView(context: Context) {
        binding = ViewStatusMarkLayoutBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)
    }

    private var eatData: StatusMarkNote? = null
    private var questionData: ArrayList<StatusMarkNoteQuestion?>? = null

    fun setViewData(data: StatusMarkNote?) {
        this.eatData = data
        setTitle(data?.name.toString())
        data?.questions?.run {
            setData(this)
        }
    }

    fun setTitle(string: String): StatusMarkLayoutView {
        binding.tvTitle.text = string
        return this
    }

    fun setData(data: ArrayList<StatusMarkNoteQuestion?>) {
        this.questionData = data
        binding.layoutButtons.removeAllViews()
        btnViews.clear()
        data.forEachIndexed { index, it ->
            val button = WidthEqualsHeightButton(context)
            val layoutParams = LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT
            )
            layoutParams.weight = 1f
            if (index != 0) {
                layoutParams.leftMargin = leftMargin
            }
            button.textSize = 12f
            button.setTextColor(resources.getColor(R.color.colorSlGray))
            button.setBackgroundResource(R.drawable.shape_bg_f5f6fa_4)
            button.layoutParams = layoutParams
            button.text = it?.name
            button.gravity = Gravity.CENTER
            button.tag = index
            button.setOnClickListener(this@StatusMarkLayoutView)
            binding.layoutButtons.addView(button)
            btnViews.put(index, button)
        }
        if (newPos == -1) return
        binding.ivStatusBg.post {
            handleAnimation(btnViews[newPos])
        }
    }

    override fun onClick(p0: View?) {
        if (!p0.isValid()) return
        if (isRunning) return
        newPos = p0?.tag as? Int ?: return
        handleAnimation(p0)
    }

    /**
     * 处理点击后的动画
     */
    private fun handleAnimation(p0: View) {
        val duration = (Math.max(abs(oldPos - newPos) * 50, 50)).toLong()
        handleStatusTips(newPos)
        startStatusLayoutAnimation(p0, newPos, duration)
        startBtnLayoutAnimation(duration)
    }

    private fun startBtnLayoutAnimation(duration: Long) {
        valueAnimator = ValueAnimator.ofFloat(oldPos.toFloat(), newPos.toFloat())
        valueAnimator?.addListener(getAnimatorListener())
        valueAnimator?.duration = duration
        valueAnimator?.interpolator = LinearInterpolator()
        valueAnimator?.addUpdateListener {
            val value = (it.animatedValue as Float).toInt()
            LogUtils.d("ValueAnimator button select:${it.animatedValue}")
            handleButtonBac(btnViews.get(btnViews.keyAt(value)), value, value > newPos)
        }
        valueAnimator?.start()
        oldPos = newPos
    }

    private fun startStatusLayoutAnimation(p0: View, index: Int, duration: Long) {
        binding.tvStatus.isVisible = true
        binding.ivStatusBg.isVisible = true
        val tvStatusNewWidth = getTvStatusWidth()
        //中心点
        val middle = (p0.width * index).toFloat() + leftMargin * index + p0.width / 2
        //气泡提示框中心点偏移计算
        newOffsetXBg = middle - tvStatusNewWidth / 2
        if (newOffsetXBg < 0) {
            newOffsetXBg = 0f
        } else if (newOffsetXBg + tvStatusNewWidth > binding.layoutButtons.width) {
            newOffsetXBg = (binding.layoutButtons.width - tvStatusNewWidth).toFloat()
        }
        //气泡提示框的倒三角中心点偏移计算
        newOffsetXCursor = middle - binding.ivStatusBg.width / 2
        oldTvStatusNewWidth = tvStatusNewWidth
        //状态提示文案移动动画
        val valueAnimator = ValueAnimator.ofFloat(oldOffsetXBg, newOffsetXBg)
        valueAnimator.duration = duration
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            LogUtils.d("ValueAnimator tvStatus:$value")
            binding.tvStatus.translationX = value
        }
        valueAnimator.start()
        //状态提示文案下的倒三角移动动画
        val valueAnimatorCursor = ValueAnimator.ofFloat(oldOffsetXCursor, newOffsetXCursor)
        valueAnimatorCursor.duration = duration
        valueAnimatorCursor.interpolator = LinearInterpolator()
        valueAnimatorCursor.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            LogUtils.d("ValueAnimator ivStatusBg:$value")
            binding.ivStatusBg.translationX = value
        }
        valueAnimatorCursor.start()
        //记录上一次的偏移量
        oldOffsetXCursor = newOffsetXCursor
        oldOffsetXBg = newOffsetXBg
    }

    /**
     * 根据位置设置相应的提示文案和对应的颜色背景
     */
    private fun handleButtonBac(
        button: TextView?,
        pos: Int,
        reset: Boolean = false,
        changeStatus: Boolean = true
    ) {
        when (pos) {
            in 0..3 -> {
                setButtonStatus(
                    reset,
                    button,
                    R.drawable.view_status_mark_1_4_r4,
                    R.drawable.view_status_mark_1_4_r12,
                    R.drawable.view_status_mark_1_4, changeStatus
                )
            }
            4 -> {
                setButtonStatus(
                    reset,
                    button,
                    R.drawable.view_status_mark_5_r4,
                    R.drawable.view_status_mark_5_r12,
                    R.drawable.view_status_mark_5, changeStatus
                )
            }
            in 5..9 -> {
                setButtonStatus(
                    reset,
                    button,
                    R.drawable.view_status_mark_6_10_r4,
                    R.drawable.view_status_mark_6_10_r12,
                    R.drawable.view_status_mark_6_10, changeStatus
                )
            }
        }
    }

    private fun setButtonStatus(
        reset: Boolean,
        button: TextView?,
        buttonRes: Int,
        statusRes: Int,
        cursorColor: Int,
        changeStatus: Boolean = true
    ) {
        if (reset) {
            button?.setBackgroundResource(R.drawable.shape_bg_f5f6fa_4)
            button?.setTextColor(resources.getColor(R.color.colorSlGray))
            return
        }
        button?.setTextColor(resources.getColor(R.color.white))
        button?.setBackgroundResource(buttonRes)
        if (!changeStatus) return
        binding.tvStatus.setBackgroundResource(statusRes)
        binding.ivStatusBg.setImageResource(cursorColor)
    }

    private fun handleStatusTips(pos: Int) {
        binding.tvStatus.text = questionData?.get(pos)?.tip
        val layoutParams = binding.tvStatus.layoutParams
        layoutParams.width = getTvStatusWidth()
        binding.tvStatus.layoutParams = layoutParams
    }

    private fun getAnimatorListener() = object : Animator.AnimatorListener {
        override fun onAnimationStart(p0: Animator?) {
            isRunning = true
            LogUtils.d("AnimatorListener onAnimationStart")
        }

        override fun onAnimationEnd(p0: Animator?) {
            for (pos in newPos downTo 0) {
                handleButtonBac(btnViews.get(btnViews.keyAt(pos)), pos, changeStatus = false)
            }
            for (pos in btnViews.size() - 1 downTo newPos + 1) {
                handleButtonBac(btnViews.get(btnViews.keyAt(pos)), pos, true, changeStatus = false)
            }
            isRunning = false
            LogUtils.d("AnimatorListener onAnimationEnd")
        }

        override fun onAnimationCancel(p0: Animator?) {
            isRunning = false
            LogUtils.d("AnimatorListener onAnimationCancel")
        }

        override fun onAnimationRepeat(p0: Animator?) {
            LogUtils.d("AnimatorListener onAnimationRepeat")
        }
    }

    private fun getTvStatusWidth(): Int {
        val paint = TextPaint()
        paint.textSize = SizeUtils.dp2px(12f).toFloat()
        return (paint.measureText(binding.tvStatus.text.toString()) + SizeUtils.dp2px(14f)).toInt()
    }
}
