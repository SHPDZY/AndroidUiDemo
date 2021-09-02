package com.example.zyuidemo.widget.likeview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

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
     * 设置缩放倍率
     *
     * @param scaleStart 缩放倍率起始值
     * @return KsgLikeView
     */
    public KsgLikeView setScaleStart(float scaleStart) {
        this.mScaleStart = scaleStart;
        return this;
    }

    /**
     * 设置是否带缩放
     *
     * @param enable 开启缩放
     * @return KsgLikeView
     */
    public KsgLikeView setScaleEnable(boolean enable) {
        this.scaleEnable = enable;
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
     * 路径动画执行时长,随机添加
     *
     * @param bezierDurationRanDom 动画执行时长
     * @return KsgLikeView
     */
    public KsgLikeView setBezierDurationRanDom(int bezierDurationRanDom) {
        this.mBezierDurationRanDom = bezierDurationRanDom;
        return this;
    }

    /**
     * 设置view缓存最少数
     *
     * @param number number
     * @return KsgLikeView
     */
    public KsgLikeView setMixViewsCacheNum(int number) {
        this.mCacheViewsMinNum = number;
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

    AnimatorSet animatorSet = null;

    /**
     * 开始执行动画
     *
     * @param child        child
     * @param layoutParams layoutParams
     */
    private void start(View child, ViewGroup.LayoutParams layoutParams) {
        // 设置进入动画
        Object tag = child.getTag();
        if (tag instanceof AnimatorSet) {
            animatorSet = (AnimatorSet) tag;
        } else {
            // 设置路径动画
            ValueAnimator curveAnimator = generateCurveAnimation(child);
            // 执行动画集合
            animatorSet = new AnimatorSet();
            if (scaleEnable) {
                AnimatorSet enterAnimator = generateEnterAnimation(child);
                animatorSet.playTogether(curveAnimator, enterAnimator);
            } else {
                animatorSet.play(curveAnimator);
            }
            animatorSet.addListener(new AnimationEndListener(child, animatorSet));
            child.setTag(animatorSet);
        }
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
                ObjectAnimator.ofFloat(child, SCALE_X, mScaleStart, 1f),
                ObjectAnimator.ofFloat(child, SCALE_Y, mScaleStart, 1f));
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
        return curveAnimator.setDuration(mBezierDuration + mRandom.nextInt(mBezierDurationRanDom));
    }

    private PointF getTogglePoint(int scale) {
        PointF pointf = new PointF();
        pointf.x = mRandom.nextInt((mViewWidth - 50));
        if (scale == 1) {
            pointf.y = nextInt(mViewHeight / 2f, mViewHeight - mPicHeight / 2f);
        } else {
            pointf.y = nextInt(mPicHeight / 2f, mViewHeight / 2f);
        }
        return pointf;
    }

    private float nextInt(float min, float max) {
        return mRandom.nextInt((int) max) % (max - min + 1) + min;
    }

    public void setMinPicSize(float minPicSize) {
        this.mMinPicSize = minPicSize;
    }
}
