package com.example.libcommon.utils;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.ColorInt;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * @author : zhangyong
 * @version :
 * @date : 2021/7/22
 * @desc :
 */
public class ColorUtils {

    /**
     * 获取带有不透明度的颜色值
     * @param colorInt  原颜色
     * @param alpha     透明度
     * @return
     */
    @ColorInt
    public static int getColor(@ColorInt int colorInt, float alpha) {
        if (alpha == 1f) {
            // 不需要透明度
            return colorInt;
        } else {
            // 设置了透明度
            return Color.argb((int) (255 * alpha), Color.red(colorInt), Color.green(colorInt), Color.blue(colorInt));
        }
    }

    /**
     * 获取修改后的颜色
     *
     * @param startColor 开始颜色
     * @param endColor   结束颜色
     * @param ratio      比率
     * @return
     */
    public static int getColor(int startColor, int endColor, float ratio) {
        int alphaStart = Color.alpha(startColor);
        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);

        int alpahEnd = Color.alpha(endColor);
        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);


        int alpha = (int) (alphaStart + ((alpahEnd - alphaStart) * ratio + 0.5));
        int red = (int) (redStart + ((redEnd - redStart) * ratio + 0.5));
        int greed = (int) (greenStart + ((greenEnd - greenStart) * ratio + 0.5));
        int blue = (int) (blueStart + ((blueEnd - blueStart) * ratio + 0.5));
        return Color.argb(alpha, red, greed, blue);
    }

    /**
     * 获取修改后的drawable
     *
     * @param drawable   drawable源文件
     * @param startColor 开始颜色
     * @param endColor   结束颜色
     * @param ratio      比率
     * @return
     */
    public static Drawable getDrawable(Drawable drawable, int startColor, int endColor, float ratio) {
        return getDrawable(drawable, getColor(startColor, endColor, ratio));
    }

    public static Drawable getDrawable(Drawable drawable, int color) {
        if (drawable instanceof GradientDrawable) {
            ((GradientDrawable) drawable).setColor(color);
            return drawable;
        }
        Drawable wrap = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrap, ColorStateList.valueOf(color));
        return wrap;
    }
}
