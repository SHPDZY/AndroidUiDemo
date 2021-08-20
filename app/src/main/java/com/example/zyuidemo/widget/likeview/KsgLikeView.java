package com.example.zyuidemo.widget.likeview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.media.Image;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.zyuidemo.R;

import java.util.Arrays;
import java.util.List;

/**
 * @Description: 飘心View
 */
public class KsgLikeView extends AnimationLayout {

    public KsgLikeView(Context context) {
        this(context, null);
    }

    public KsgLikeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initTypedArray(attrs);
    }

    private void initTypedArray(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.KsgLikeView);
        // 进入动画时长
        this.mEnterScaleDuration = typedArray.getInteger(R.styleable.KsgLikeView_ksg_enter_duration, 1500);
        // 贝塞尔动画时长
        this.mBezierDuration = typedArray.getInteger(R.styleable.KsgLikeView_ksg_curve_duration, 4500);
        // 回收
        typedArray.recycle();
    }

    /**
     * 进入动画执行时长
     *
     * @param enterScaleDuration 动画执行时长
     * @return KsgLikeView
     */
    public KsgLikeView setEnterScaleDuration(int enterScaleDuration) {
        this.mEnterScaleDuration = enterScaleDuration;
        return this;
    }

    /**
     * 路径动画执行时长
     *
     * @param bezierDuration 动画执行时长
     * @return KsgLikeView
     */
    public KsgLikeView setBezierDuration(int bezierDuration) {
        this.mBezierDuration = bezierDuration;
        return this;
    }

    /**
     * 添加 资源文件
     *
     * @param resId resId
     */
    @Override
    public KsgLikeView addLikeImage(int resId) {
        this.addLikeImages(resId);
        return this;
    }

    /**
     * 添加 资源文件组
     *
     * @param resIds resIds
     */
    @Override
    public KsgLikeView addLikeImages(Integer... resIds) {
        this.addLikeImages(Arrays.asList(resIds));
        return this;
    }

    /**
     * 添加 资源文件列表
     *
     * @param resIds resIds
     */
    @Override
    public KsgLikeView addLikeImages(List<Integer> resIds) {
        this.mLikeRes.addAll(resIds);
        return this;
    }

    /**
     * 设置 资源文件列表
     *
     * @param resIds resIds
     */
    @Override
    public KsgLikeView setLikeImages(List<Integer> resIds) {
        this.mLikeRes.clear();
        return addLikeImages(resIds);
    }

    /**
     * 添加 发送
     */
    @Override
    public KsgLikeView addFavor() {
        // 非空验证
        if (mLikeRes.isEmpty()) {
            throw new NullPointerException("Missing resource file！");
        }
        // 随机获取一个资源
        int favorRes = Math.abs(mLikeRes.get(mRandom.nextInt(mLikeRes.size())));
        // 生成 配置参数
        ViewGroup.LayoutParams layoutParams;
        // 创建一个资源View
        ImageView favorView = (ImageView) getCacheView();
        if (favorView == null) {
            favorView = new AppCompatImageView(getContext());
            layoutParams = createLayoutParams(favorRes);
        } else {
            layoutParams = favorView.getLayoutParams();
            mPicWidth = favorView.getWidth();
            mPicHeight = favorView.getHeight();
        }
        favorView.setImageResource(favorRes);
        // 开始执行动画
        this.start(favorView, layoutParams);
        return this;
    }

    /**
     * 开始执行动画
     *
     * @param child        child
     * @param layoutParams layoutParams
     */
    private void start(View child, ViewGroup.LayoutParams layoutParams) {
        // 设置进入动画
        AnimatorSet enterAnimator = generateEnterAnimation(child);
        // 设置路径动画
        ValueAnimator curveAnimator = generateCurveAnimation(child);
        // 执行动画集合
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(curveAnimator, enterAnimator);
        animatorSet.addListener(new AnimationEndListener(child, animatorSet));
        animatorSet.start();
        // add父布局
        super.addView(child, layoutParams);
    }

    /**
     * 进入动画
     *
     * @return 动画集合
     */
    private AnimatorSet generateEnterAnimation(View child) {
        AnimatorSet enterAnimation = new AnimatorSet();
        enterAnimation.playTogether(
                ObjectAnimator.ofFloat(child, SCALE_X, 0.2f, 1f),
                ObjectAnimator.ofFloat(child, SCALE_Y, 0.2f, 1f));
        // 加一些动画差值器
        enterAnimation.setInterpolator(new LinearInterpolator());
        return enterAnimation.setDuration(mEnterScaleDuration);
    }

    /**
     * 贝赛尔曲线动画
     *
     * @return 动画集合
     */
    private ValueAnimator generateCurveAnimation(View child) {
        // 起点 坐标
        PointF pointStart = new PointF((mViewWidth - mPicWidth) / 2, mViewHeight - mPicHeight);
        // 终点 坐标
        PointF pointEnd = new PointF(((mViewWidth - mPicWidth) / 2) + ((mRandom.nextBoolean() ? 1 : -1) * mRandom.nextInt(100)), 0);
        // 属性动画
        PointF pointF1 = getTogglePoint(1);
        PointF pointF2 = getTogglePoint(2);
        ValueAnimator curveAnimator = ValueAnimator.ofObject(mEvaluatorRecord.getCurrentPath(pointF1, pointF2), pointStart, pointEnd);
        curveAnimator.addUpdateListener(new CurveUpdateLister(child));
        curveAnimator.setInterpolator(new LinearInterpolator());
        return curveAnimator.setDuration(mBezierDuration);
    }

    private PointF getTogglePoint(int scale) {
        PointF pointf = new PointF();
        // 减去100 是为了控制 x轴活动范围
        pointf.x = mRandom.nextInt((mViewWidth - 50));
        // 再Y轴上 为了确保第二个控制点 在第一个点之上,我把Y分成了上下两半
        pointf.y = (float) mRandom.nextInt((mViewHeight - 50)) / scale;
        return pointf;
    }

    public void setMinPicSize(float minPicSize) {
        this.mMinPicSize = minPicSize;
    }
}
