package com.example.libcommon.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class WidthEqualsHeightButton extends AppCompatTextView {
    public WidthEqualsHeightButton(Context context) {
        super(context);
    }

    public WidthEqualsHeightButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WidthEqualsHeightButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
