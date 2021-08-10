package com.example.libcommon.utils

import android.animation.*
import android.view.View
import android.view.animation.*
import com.boohee.core.util.extensionfunc.*


object AnimationUtils {

    /**
     * 动画放大缩小View的高度，利用值动画实现
     */
    fun setWidgetHeight(
        view: View,
        fromHeight: Int,
        toHeight: Int,
        duration: Long = 250L,
        interpolator: Interpolator = DecelerateInterpolator(),
        listenerAdapter: AnimatorListenerAdapter? = null
    ) {
        val animator = ValueAnimator.ofInt(fromHeight, toHeight)
        animator.duration = duration
        animator.interpolator = interpolator
        animator.addUpdateListener {
            setWidgetHeight(view, it.animatedValue as Int)
        }
        listenerAdapter?.let {
            animator.addListener(listenerAdapter)
        }
        animator.start()
    }

    /**
     * 动画放大缩小View的高度，利用值动画实现
     */
    fun scaleView(view: View?): AnimatorSet? {
        view ?: return null
        val animatorSet = AnimatorSet()//组合动画
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.1f, 1f)

        scaleX.repeatCount = -1
        scaleY.repeatCount = -1

        animatorSet.duration = 1200
        animatorSet.interpolator = LinearInterpolator()
        animatorSet.play(scaleX).with(scaleY)//两个动画同时开始
        animatorSet.start()
        return animatorSet
    }

    /**
     * 平移渐变动画
     *
     * @param view         view
     * @param fromX        开始位置
     * @param toX          结束位置
     * @param duration     动画时间
     */
    fun translationAlphaView(
        view: View, fromX: Float, toX: Float,
        fromAlpha: Float, toAlpha: Float, duration: Long = 300L
    ) {
        translationAlphaView(view, fromX, toX, fromAlpha, toAlpha, duration, null)
    }

    /**
     * 平移渐变动画
     *
     * @param view         view
     * @param fromX        开始位置
     * @param toX          结束位置
     * @param duration     动画时间
     */
    fun translationAlphaView(
        view: View,
        fromX: Float,
        toX: Float,
        fromAlpha: Float,
        toAlpha: Float,
        duration: Long = 300L,
        listener: Animator.AnimatorListener?
    ) {
        val alphaAnimation = ObjectAnimator.ofFloat(view, "alpha", fromAlpha, toAlpha)
        val translateAnimation = ObjectAnimator.ofFloat(view, "translationX", fromX, toX)
        val set = AnimatorSet()
        set.playTogether(alphaAnimation, translateAnimation)
        set.duration = duration
        if (listener != null) {
            set.addListener(listener)
        }
        set.start()
    }


    /**
     * 设置控件高
     *
     * @param view
     * @param height
     */
    private fun setWidgetHeight(view: View, height: Int) {
        val params = view.layoutParams
        params.height = height
        view.layoutParams = params
    }


    fun translationYAlphaView(
        view: View,
        fromY: Float,
        toY: Float,
        fromAlpha: Float,
        toAlpha: Float,
        duration: Long = 300L,
        listener: Animator.AnimatorListener?
    ) {
        val alphaAnimation = ObjectAnimator.ofFloat(view, "alpha", fromAlpha, toAlpha)
        val translateAnimation = ObjectAnimator.ofFloat(view, "translationY", fromY, toY)
        val set = AnimatorSet()
        set.playTogether(alphaAnimation, translateAnimation)
        set.duration = duration
        if (listener != null) {
            set.addListener(listener)
        }
        set.start()
    }

    fun startShakeByPropertyAnim(
        view: View?,
        scaleSmall: Float,
        scaleLarge: Float,
        shakeDegrees: Float,
        duration: Long
    ) {
        if (view == null) {
            return
        }
        //TODO 验证参数的有效性

        //先变小后变大
        val scaleXValuesHolder = PropertyValuesHolder.ofKeyframe(
            View.SCALE_X,
            Keyframe.ofFloat(0f, 1.0f),
            Keyframe.ofFloat(0.25f, scaleSmall),
            Keyframe.ofFloat(0.5f, scaleLarge),
            Keyframe.ofFloat(0.75f, scaleLarge),
            Keyframe.ofFloat(1.0f, 1.0f)
        )
        val scaleYValuesHolder = PropertyValuesHolder.ofKeyframe(
            View.SCALE_Y,
            Keyframe.ofFloat(0f, 1.0f),
            Keyframe.ofFloat(0.25f, scaleSmall),
            Keyframe.ofFloat(0.5f, scaleLarge),
            Keyframe.ofFloat(0.75f, scaleLarge),
            Keyframe.ofFloat(1.0f, 1.0f)
        )

        //先往左再往右
        val rotateValuesHolder = PropertyValuesHolder.ofKeyframe(
            View.ROTATION,
            Keyframe.ofFloat(0f, 0f),
            Keyframe.ofFloat(0.1f, -shakeDegrees),
            Keyframe.ofFloat(0.2f, shakeDegrees),
            Keyframe.ofFloat(0.3f, -shakeDegrees),
            Keyframe.ofFloat(0.4f, shakeDegrees),
            Keyframe.ofFloat(0.5f, -shakeDegrees),
            Keyframe.ofFloat(0.6f, shakeDegrees),
            Keyframe.ofFloat(0.7f, -shakeDegrees),
            Keyframe.ofFloat(0.8f, shakeDegrees),
            Keyframe.ofFloat(0.9f, -shakeDegrees),
            Keyframe.ofFloat(1.0f, 0f)
        )

        val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            view,
            scaleXValuesHolder,
            scaleYValuesHolder,
            rotateValuesHolder
        )
        objectAnimator.duration = duration
        objectAnimator.repeatCount = -1
        objectAnimator.start()

    }

    /**
     * 放大View
     *
     * @param view     view
     * @param duration 时间
     */
    fun scaleBigView(view: View, duration: Int) {
        val animatorX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.15f)
        val animatorY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.15f)
//        val translationY = ObjectAnimator.ofFloat(view, "translationY", 0f,0f-dp2px(3f))
        val set = AnimatorSet()
//        set.play(animatorX).with(animatorY).with(translationY)
        set.play(animatorX).with(animatorY)
        set.duration = duration.toLong()
        set.interpolator = AnticipateOvershootInterpolator()
        set.start()
    }

    /**
     * 缩小View
     *
     * @param view     view
     * @param duration 时间
     */
    fun scaleSmallView(view: View, duration: Int) {
        val animatorX = ObjectAnimator.ofFloat(view, "scaleX", 1.15f, 1f)
        val animatorY = ObjectAnimator.ofFloat(view, "scaleY", 1.15f, 1f)
        val set = AnimatorSet()
        set.play(animatorX).with(animatorY)
        set.duration = duration.toLong()
        set.interpolator = AnticipateOvershootInterpolator()
        set.start()
    }

    /**
     * 放大View
     *
     * @param view     view
     * @param duration 时间
     */
    fun translationView(view: View, duration: Int) {
        val translationY = ObjectAnimator.ofFloat(view, "translationY", 0f, 0f - dp2px(3f))
        translationY.duration = duration.toLong()
        translationY.interpolator = AnticipateOvershootInterpolator()
        translationY.start()
    }

    /**
     * 左右摆动动画
     *
     * @param view     view
     * @param duration 时间
     */
    fun rotationView(view: View, duration: Int): ObjectAnimator? {
        view.pivotX = dp2px(30f) / 2f
        view.pivotY = dp2px(30f) / 2f
        val rotation = ObjectAnimator.ofFloat(
            view,
            "rotation",
            0f,
            30f,
            0f,
            -30f,
            0f,
            22f,
            0f,
            -22f,
            0f,
            8f,
            0f,
            -8f,
            0f
        )
        rotation.duration = duration.toLong()
        rotation.interpolator = LinearInterpolator()
        rotation.repeatCount = -1
        return rotation
    }

    /**
     * 渐显view
     *
     * @param view     view
     * @param duration 时间
     */
    fun alphaShowView(view: View, duration: Long = 300): ObjectAnimator {
        return alphaView(view, 0f, 1f, duration, startCallBack = {
            view.alpha = 0f
            view.setVisible()
        })
    }

    /**
     * 渐隐view
     *
     * @param view     view
     * @param duration 时间
     */
    fun alphaHideView(view: View, duration: Long = 300): ObjectAnimator {
        return alphaView(view, 1f, 0f, duration, endCallBack = { view.setGone() })
    }

    /**
     * 设置view的alpha动画
     */
    fun alphaView(
        view: View,
        from: Float,
        to: Float,
        duration: Long = 300,
        startCallBack: (() -> Unit?)? = null,
        endCallBack: (() -> Unit?)? = null
    ): ObjectAnimator {
        val anim = ObjectAnimator.ofFloat(view, "alpha", from, to)
        anim.duration = duration
        anim.interpolator = LinearInterpolator()
        if (endCallBack != null || startCallBack != null) {
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)
                    startCallBack?.invoke()
                }

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    endCallBack?.invoke()
                }
            })
        }
        anim.start()
        return anim
    }

    /**
     * Y轴移动view
     */
    fun translationYView(
        view: View,
        from: Float,
        to: Float,
        duration: Long = 300,
        startCallBack: (() -> Unit?)? = null,
        endCallBack: (() -> Unit?)? = null
    ): ObjectAnimator {
        val anim = ObjectAnimator.ofFloat(view, "translationY", from, to)
        anim.duration = duration
        anim.interpolator = LinearInterpolator()
        if (endCallBack != null || startCallBack != null) {
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)
                    startCallBack?.invoke()
                }

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    endCallBack?.invoke()
                }
            })
        }
        anim.start()
        return anim
    }


    fun translationXView(
        view: View,
        from: Float,
        to: Float,
        duration: Long = 300,
        startCallBack: (() -> Unit?)? = null,
        endCallBack: (() -> Unit?)? = null
    ): ObjectAnimator {
        val anim = ObjectAnimator.ofFloat(view, "translationX", from, to)
        anim.duration = duration
        anim.interpolator = LinearInterpolator()
        if (endCallBack != null || startCallBack != null) {
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)
                    startCallBack?.invoke()
                }

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    endCallBack?.invoke()
                }
            })
        }
        anim.start()
        return anim
    }


    /**
     * X轴移动渐隐渐现view
     */
    fun translationXAlphaView(
        view: View,
        tranFrom: Float,
        tranTo: Float,
        alphaFrom: Float,
        alphaTo: Float,
        duration: Long = 300,
        startCallBack: (() -> Unit?)? = null,
        endCallBack: (() -> Unit?)? = null
    ): AnimatorSet {
        val animationSet = AnimatorSet()
        val alphaAnimation =
            ObjectAnimator.ofFloat(view, "alpha", alphaFrom, alphaTo)
        val translateAnimation = ObjectAnimator.ofFloat(view, "translationX", tranFrom, tranTo)
        if (endCallBack != null || startCallBack != null) {
            animationSet.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)
                    startCallBack?.invoke()
                }

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    endCallBack?.invoke()
                }
            })
        }
        animationSet.duration = duration
        animationSet.playTogether(alphaAnimation,translateAnimation)
        animationSet.start()
        return animationSet
    }
}
