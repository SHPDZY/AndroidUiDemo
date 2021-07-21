package com.example.libcommon.widget.shadow;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;

import com.example.libcommon.R;


/**
 * 阴影控件
 */

public class ShadowLayout extends FrameLayout {
    private int mBackGroundColor;
    private int mShadowColor;
    private float mShadowLimit;
    private float mCornerRadius;
    private float mDx;
    private float mDy;
    private boolean leftShow;
    private boolean rightShow;
    private boolean topShow;
    private boolean bottomShow;
    private Paint shadowPaint;
    private Paint paint;

    private int leftPadding;
    private int topPadding;
    private int rightPadding;
    private int bottomPadding;
    //阴影布局子空间区域
    private RectF rectf = new RectF();

    private boolean isShowShadow = true;
    private boolean isSym;

    public ShadowLayout(Context context) {
        this(context, null);
    }

    public ShadowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public ShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    //动态设置x轴偏移量
    public void setMDx(float mDx) {
        if (Math.abs(mDx) > mShadowLimit) {
            if (mDx > 0) {
                this.mDx = mShadowLimit;
            } else {
                this.mDx = -mShadowLimit;
            }
        } else {
            this.mDx = mDx;
        }
        setPading();
    }

    //动态设置y轴偏移量
    public void setMDy(float mDy) {
        if (Math.abs(mDy) > mShadowLimit) {
            if (mDy > 0) {
                this.mDy = mShadowLimit;
            } else {
                this.mDy = -mShadowLimit;
            }
        } else {
            this.mDy = mDy;
        }
        setPading();
    }


    public float getmCornerRadius() {
        return mCornerRadius;
    }

    //动态设置 圆角属性
    public void setmCornerRadius(int mCornerRadius) {
        this.mCornerRadius = mCornerRadius;
        if (getWidth() != 0 && getHeight() != 0) {
            setBackgroundCompat(getWidth(), getHeight());
        }
    }

    public float getmShadowLimit() {
        return mShadowLimit;
    }

    //动态设置阴影扩散区域
    public void setmShadowLimit(int mShadowLimit) {
        this.mShadowLimit = mShadowLimit;
        setPading();
    }

    //动态设置阴影颜色值
    public void setmShadowColor(int mShadowColor) {
        this.mShadowColor = mShadowColor;
        if (getWidth() != 0 && getHeight() != 0) {
            setBackgroundCompat(getWidth(), getHeight());
        }
    }


    public void setLeftShow(boolean leftShow) {
        this.leftShow = leftShow;
        setPading();
    }

    public void setRightShow(boolean rightShow) {
        this.rightShow = rightShow;
        setPading();
    }

    public void setTopShow(boolean topShow) {
        this.topShow = topShow;
        setPading();
    }

    public void setBottomShow(boolean bottomShow) {
        this.bottomShow = bottomShow;
        setPading();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (w > 0 && h > 0) {
            setBackgroundCompat(w, h);
        }
    }

    private void initView(Context context, AttributeSet attrs) {
        initAttributes(attrs);
        shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setStyle(Paint.Style.FILL);
        //矩形画笔
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mBackGroundColor);
        setPading();
    }


    public int dip2px(float dipValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public void setPading() {
        if (isShowShadow && mShadowLimit > 0) {
            //控件区域是否对称，默认是对称。不对称的话，那么控件区域随着阴影区域走
            if (isSym) {
                int xPadding = (int) (mShadowLimit + Math.abs(mDx));
                int yPadding = (int) (mShadowLimit + Math.abs(mDy));

                if (leftShow) {
                    leftPadding = xPadding;
                } else {
                    leftPadding = 0;
                }

                if (topShow) {
                    topPadding = yPadding;
                } else {
                    topPadding = 0;
                }


                if (rightShow) {
                    rightPadding = xPadding;
                } else {
                    rightPadding = 0;
                }

                if (bottomShow) {
                    bottomPadding = yPadding;
                } else {
                    bottomPadding = 0;
                }
            } else {
                if (Math.abs(mDy) > mShadowLimit) {
                    if (mDy > 0) {
                        mDy = mShadowLimit;
                    } else {
                        mDy = 0 - mShadowLimit;
                    }
                }


                if (Math.abs(mDx) > mShadowLimit) {
                    if (mDx > 0) {
                        mDx = mShadowLimit;
                    } else {
                        mDx = 0 - mShadowLimit;
                    }
                }

                if (topShow) {
                    topPadding = (int) (mShadowLimit - mDy);
                } else {
                    topPadding = 0;
                }

                if (bottomShow) {
                    bottomPadding = (int) (mShadowLimit + mDy);
                } else {
                    bottomPadding = 0;
                }


                if (rightShow) {
                    rightPadding = (int) (mShadowLimit - mDx);
                } else {
                    rightPadding = 0;
                }


                if (leftShow) {
                    leftPadding = (int) (mShadowLimit + mDx);
                } else {
                    leftPadding = 0;
                }
            }
            setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
        }
    }

    @SuppressWarnings("deprecation")
    private void setBackgroundCompat(int w, int h) {
        if (isShowShadow) {
            //判断传入的颜色值是否有透明度
            isAddAlpha(mShadowColor);
            Bitmap bitmap = createShadowBitmap(w, h, mCornerRadius, mShadowLimit, mDx, mDy, mShadowColor, Color.TRANSPARENT);
            BitmapDrawable drawable = new BitmapDrawable(bitmap);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                setBackgroundDrawable(drawable);
            } else {
                setBackground(drawable);
            }
        }

    }


    private void initAttributes(AttributeSet attrs) {
        TypedArray attr = getContext().obtainStyledAttributes(attrs, R.styleable.ShadowLayout);
        if (attr == null) {
            return;
        }

        try {
            //默认是显示
            isShowShadow = !attr.getBoolean(R.styleable.ShadowLayout_hl_shadowHidden, false);
            leftShow = !attr.getBoolean(R.styleable.ShadowLayout_hl_shadowHiddenLeft, false);
            rightShow = !attr.getBoolean(R.styleable.ShadowLayout_hl_shadowHiddenRight, false);
            bottomShow = !attr.getBoolean(R.styleable.ShadowLayout_hl_shadowHiddenBottom, false);
            topShow = !attr.getBoolean(R.styleable.ShadowLayout_hl_shadowHiddenTop, false);
            mCornerRadius = attr.getDimension(R.styleable.ShadowLayout_hl_cornerRadius, 0f);

            //默认扩散区域宽度
            mShadowLimit = attr.getDimension(R.styleable.ShadowLayout_hl_shadowLimit, 0);
            if (mShadowLimit == 0) {
                //如果阴影没有设置阴影扩散区域，那么默认隐藏阴影
                isShowShadow = false;
            }

            //x轴偏移量
            mDx = attr.getDimension(R.styleable.ShadowLayout_hl_shadowOffsetX, 0);
            //y轴偏移量
            mDy = attr.getDimension(R.styleable.ShadowLayout_hl_shadowOffsetY, 0);
            mShadowColor = attr.getColor(R.styleable.ShadowLayout_hl_shadowColor, Color.DKGRAY);

            isSym = attr.getBoolean(R.styleable.ShadowLayout_hl_shadowSymmetry, true);

            //背景颜色的点击(默认颜色为白色)
            mBackGroundColor = Color.WHITE;

            Drawable background = attr.getDrawable(R.styleable.ShadowLayout_hl_layoutBackground);
            if (background != null) {
                if (background instanceof ColorDrawable) {
                    ColorDrawable colordDrawable = (ColorDrawable) background;
                    mBackGroundColor = colordDrawable.getColor();
                }
            }

        } finally {
            attr.recycle();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }


    private Bitmap createShadowBitmap(int shadowWidth, int shadowHeight, float cornerRadius, float shadowRadius,
                                      float dx, float dy, int shadowColor, int fillColor) {
        //优化阴影bitmap大小,将尺寸缩小至原来的1/4。
        dx = dx / 4;
        dy = dy / 4;
        shadowWidth = shadowWidth / 4;
        shadowHeight = shadowHeight / 4;
        cornerRadius = cornerRadius / 4;
        shadowRadius = shadowRadius / 4;

        Bitmap output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(output);

        //这里缩小limit的是因为，setShadowLayer后会将bitmap扩散到shadowWidth，shadowHeight
        RectF shadowRect = new RectF(
                shadowRadius,
                shadowRadius,
                shadowWidth - shadowRadius,
                shadowHeight - shadowRadius);

        if (isSym) {
            if (dy > 0) {
                shadowRect.top += dy;
                shadowRect.bottom -= dy;
            } else if (dy < 0) {
                shadowRect.top += Math.abs(dy);
                shadowRect.bottom -= Math.abs(dy);
            }

            if (dx > 0) {
                shadowRect.left += dx;
                shadowRect.right -= dx;
            } else if (dx < 0) {

                shadowRect.left += Math.abs(dx);
                shadowRect.right -= Math.abs(dx);
            }
        } else {
            shadowRect.top -= dy;
            shadowRect.bottom -= dy;
            shadowRect.right -= dx;
            shadowRect.left -= dx;
        }


        shadowPaint.setColor(fillColor);
        if (!isInEditMode()) {//dx  dy
            shadowPaint.setShadowLayer(shadowRadius, dx, dy, shadowColor);
        }
        canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint);
        return output;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rectf.left = leftPadding;
        rectf.top = topPadding;
        rectf.right = getWidth() - rightPadding;
        rectf.bottom = getHeight() - bottomPadding;
        int trueHeight = (int) (rectf.bottom - rectf.top);
        //如果都为0说明，没有设置特定角，那么按正常绘制
        if (getChildAt(0) != null) {
            if (mCornerRadius > trueHeight / 2) {
                //画圆角矩形
                canvas.drawRoundRect(rectf, trueHeight / 2, trueHeight / 2, paint);
            } else {
                canvas.drawRoundRect(rectf, mCornerRadius, mCornerRadius, paint);
            }
        }

    }


    public void isAddAlpha(int color) {
        //获取单签颜色值的透明度，如果没有设置透明度，默认加上#2a
        if (Color.alpha(color) == 255) {
            String red = Integer.toHexString(Color.red(color));
            String green = Integer.toHexString(Color.green(color));
            String blue = Integer.toHexString(Color.blue(color));

            if (red.length() == 1) {
                red = "0" + red;
            }

            if (green.length() == 1) {
                green = "0" + green;
            }

            if (blue.length() == 1) {
                blue = "0" + blue;
            }
            String endColor = "#2a" + red + green + blue;
            mShadowColor = convertToColorInt(endColor);
        }
    }


    public static int convertToColorInt(String argb)
            throws IllegalArgumentException {

        if (!argb.startsWith("#")) {
            argb = "#" + argb;
        }

        return Color.parseColor(argb);
    }
}

