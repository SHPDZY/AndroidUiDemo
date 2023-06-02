/*
 * Created by loody 7/27/17 3:58 PM .
 * Copyright (c) 2017 Boohee, Inc. All rights reserved.
 */

package com.example.zyuidemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class SecondFloorRefreshView extends View {

    private Paint linePaint;
    private Path linePath;

    private int height, width;
    int quadY = 1;

    public SecondFloorRefreshView(Context context) {
        this(context, null);
    }

    public SecondFloorRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SecondFloorRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        linePaint = new Paint();
        linePath = new Path();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.parseColor("#00CDA2"));
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setXfermode(null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) {
            quadY = 100;
        }
    }

    public void setQuadY(int quadY) {
        this.quadY = quadY;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
        width = w;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        linePath.reset();
        linePath.moveTo(0, 0);
        android.graphics.Point p1 = new android.graphics.Point();
        p1.set(width / 2, quadY);
        linePath.quadTo(p1.x, p1.y, width, 0);
        canvas.drawPath(linePath, linePaint);

    }

    private int dp2px(float value) {
        float v = getContext().getResources().getDisplayMetrics().density;
        return (int) (v * value + 0.5f);
    }
}
