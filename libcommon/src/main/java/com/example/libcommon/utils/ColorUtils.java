package com.example.libcommon.utils;

import android.graphics.Color;

/**
 * @author : zhangyong
 * @version :
 * @date : 2021/7/22
 * @desc :
 */
public class ColorUtils {
    public static int getColor(int startColor, int endColor, float radio) {
        int alphaStart = Color.alpha(startColor);
        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);

        int alpahEnd = Color.alpha(endColor);
        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);


        int alpha = (int) (alphaStart + ((alpahEnd - alphaStart) * radio + 0.5));
        int red = (int) (redStart + ((redEnd - redStart) * radio + 0.5));
        int greed = (int) (greenStart + ((greenEnd - greenStart) * radio + 0.5));
        int blue = (int) (blueStart + ((blueEnd - blueStart) * radio + 0.5));
        return Color.argb(alpha, red, greed, blue);
    }
}
