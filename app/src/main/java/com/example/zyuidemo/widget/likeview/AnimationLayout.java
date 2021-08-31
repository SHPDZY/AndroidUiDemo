package com.example.zyuidemo.widget.likeview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.zyuidemo.widget.likeview.evaluator.CurveEvaluatorRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Description:
 */
public abstract class AnimationLayout extends FrameLayout implements IAnimationLayout {

    protected final Random mRandom = new Random();

    protected final List<Integer> mLikeRes = new ArrayList<>();

    protected int mEnterScaleDuration;

    protected int mBezierDuration;

    protected int mViewWidth, mViewHeight;

    protected float mPicWidth, mPicHeight;

    protected float mMinPicSize = dp2px(30f);

    protected List<AnimatorSet> mAnimatorSets;

    protected ArrayList<View> mCacheViews;

    protected CurveEvaluatorRecord mEvaluatorRecord;

    public AnimationLayout(@NonNull Context context) {
        this(context, null);
    }

    public AnimationLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 初始化
        this.init();
    }

    /**
     * 初始化
     */
    protected void init() {
        // 动画集合
        if (mAnimatorSets == null) {
            this.mAnimatorSets = new ArrayList<>();
        }
        // 贝塞尔曲线缓存类
        if (mEvaluatorRecord == null) {
            this.mEvaluatorRecord = new CurveEvaluatorRecord();
        }
        if (mCacheViews == null) {
            this.mCacheViews = new ArrayList<>();
        }
    }

    /**
     * 获取资源图片信息
     *
     * @param resId 资源Id
     */
    public void getPictureInfo(@DrawableRes int resId) {
        // 获取图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 只读取图片，不加载到内存中
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getContext().getResources(), resId, options);
        // 获取图片的宽高
        this.mPicWidth = Math.max(mMinPicSize, options.outWidth);
        this.mPicHeight = Math.max(mMinPicSize, options.outHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mViewWidth = getMeasuredWidth();
        this.mViewHeight = getMeasuredHeight();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        this.mViewWidth = getMeasuredWidth();
        this.mViewHeight = getMeasuredHeight();
    }

    /**
     * 贝塞尔曲线路径更新事件
     */
    protected static class CurveUpdateLister implements ValueAnimator.AnimatorUpdateListener {

        private final View mChild;

        protected CurveUpdateLister(View child) {
            this.mChild = child;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            PointF value = (PointF) animation.getAnimatedValue();
            this.mChild.setX(value.x);
            this.mChild.setY(value.y);
            this.mChild.setAlpha(1 - animation.getAnimatedFraction());
        }
    }

    /**
     * 动画结束监听器,用于释放无用的资源
     */
    protected class AnimationEndListener extends AnimatorListenerAdapter {

        private View mChild;

        private final AnimatorSet mAnimatorSet;

        protected AnimationEndListener(View child, AnimatorSet animatorSet) {
            this.mChild = child;
            this.mAnimatorSet = animatorSet;
            // 缓存
            mAnimatorSets.add(mAnimatorSet);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            // 移除View
            removeView(mChild);
            // 移除缓存
            mAnimatorSets.remove(mAnimatorSet);
            // 缓存View
            cacheView(mChild);
        }
    }

    protected void cacheView(View view) {
        mCacheViews.add(view);
    }

    protected View getCacheView() {
        return mCacheViews.size() == 0 ? null : mCacheViews.remove(0);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 销毁资源
        this.destroy();
    }

    /**
     * 销毁资源
     */
    public void destroy() {
        // 移除所有View
        this.removeAllViews();
        // 取消动画 释放资源
        if (mAnimatorSets != null) {
            // 取消动画
            for (AnimatorSet animatorSet : mAnimatorSets) {
                animatorSet.getListeners().clear();
                animatorSet.cancel();
            }
            this.mAnimatorSets.clear();
            this.mAnimatorSets = null;
        }
        // 清空缓存路径
        if (mEvaluatorRecord != null) {
            this.mEvaluatorRecord.destroy();
            this.mEvaluatorRecord = null;
        }
    }

    /**
     * 生成 配置参数
     */
    protected LayoutParams createLayoutParams(int crystalLeaf) {
        // 获取图片信息
        this.getPictureInfo(crystalLeaf);
        // 初始化布局参数
        return new LayoutParams((int) mPicWidth, (int) mPicHeight, Gravity.BOTTOM | Gravity.CENTER);
    }

    public static float dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }
}