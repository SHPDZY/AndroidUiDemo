package com.example.zyuidemo.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author : zhangyong
 * @version :
 * @date : 2023/6/2
 * @desc :
 */
public class WaveView extends View {
    private ValueAnimator animator;
    private int startX;

    public WaveView(Context context) {
        super(context);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private Paint linePaint;
    private Path linePath;
    private int height, width, widthHalf, widthQtr;
    private float percent = 0.5f;
    private float offsetY;

    private void init(Context context) {
        linePath = new Path();
        linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#00CDA2"));
        linePaint.setStyle(Paint.Style.FILL);

    }

    public void start() {
        if (animator == null) {
            animator = ValueAnimator.ofFloat(0.1f, 0.6f);
            animator.setDuration(10000);
            animator.addUpdateListener(animation -> {
                Float value = (Float) animation.getAnimatedValue();
                setPercent(value);
            });
        }
        animator.start();
    }

    public void setPercent(Float value) {
        percent = value;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) {
            percent = 0.5f;
        }
        start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
        width = w;
        widthHalf = width / 2;
        widthQtr = width / 8;
        startX = -width;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        linePath.reset();
        offsetY = height - height * percent;
        linePath.moveTo(startX, offsetY);
        linePath.cubicTo(startX + widthQtr, Math.max(offsetY - 100, 1f), startX + widthQtr * 3f, Math.min(offsetY + 100, height - 1), startX + widthQtr * 4f, offsetY);
        linePath.cubicTo(startX + widthQtr * 5f, Math.max(offsetY - 100, 1f), startX + widthQtr * 7f, Math.min(offsetY + 100, height - 1), startX + widthQtr * 8f, offsetY);
        linePath.cubicTo(startX + widthQtr * 9f, Math.max(offsetY - 100, 1f), startX + widthQtr * 11f, Math.min(offsetY + 100, height - 1), startX + widthQtr * 12f, offsetY);
        linePath.cubicTo(startX + widthQtr * 13f, Math.max(offsetY - 100, 1f), startX + widthQtr * 15f, Math.min(offsetY + 100, height - 1), startX + widthQtr * 16f, offsetY);
        linePath.cubicTo(startX + widthQtr * 17f, Math.max(offsetY - 100, 1f), startX + widthQtr * 19f, Math.min(offsetY + 100, height - 1), startX + widthQtr * 20f, offsetY);
        linePath.cubicTo(startX + widthQtr * 21f, Math.max(offsetY - 100, 1f), startX + widthQtr * 23f, Math.min(offsetY + 100, height - 1), startX + widthQtr * 24f, offsetY);
        linePath.lineTo(startX + widthQtr * 24f, height);
        linePath.lineTo(startX, height);
        linePath.close();
        canvas.drawPath(linePath, linePaint);
        startX += 5f;
        if (startX >= 0) {
            startX = -width;
        }
        invalidate();
    }
}
