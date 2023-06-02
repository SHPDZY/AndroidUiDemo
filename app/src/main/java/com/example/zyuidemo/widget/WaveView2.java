package com.example.zyuidemo.widget;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class WaveView2 extends View {

    private Path mPath;
    private Paint mPaint;
    private int mWaveColor = Color.BLUE; // 波浪颜色
    private float mWaveAmplitude = 50; // 波浪振幅
    private float mWaveLength = 400; // 波浪长度
    private float mWaveSpeed = 0.5f; // 波浪速度
    private float mWaveOffset; // 波浪偏移量

    public WaveView2(Context context) {
        super(context);
        init();
    }

    public WaveView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPath = new Path();

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mWaveColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();

        int width = getWidth();
        int height = getHeight();

        int halfWaveLength = (int) (mWaveLength / 2);
        float y;

        mPath.moveTo(-mWaveLength + mWaveOffset, height / 2);

        for (int x = -halfWaveLength; x <= width + halfWaveLength; x += mWaveLength) {
            y = (float) (mWaveAmplitude * Math.sin((x + mWaveOffset) * 2 * Math.PI / mWaveLength));
            mPath.rQuadTo(mWaveLength / 4, -y, mWaveLength / 2, 0);
            mPath.rQuadTo(mWaveLength / 4, y, mWaveLength / 2, 0);
        }

        mPath.lineTo(width, height);
        mPath.lineTo(0, height);
        mPath.close();

        canvas.drawPath(mPath, mPaint);

        mWaveOffset += mWaveSpeed;
        if (mWaveOffset > mWaveLength) {
            mWaveOffset -= mWaveLength;
        }

        invalidate();
    }

}

