package com.example.libcommon.utils;

import android.graphics.PointF;

/**
 * 获取贝塞尔曲线上点的坐标工具类
 */

public class BezierUtil {

    /**
     * 二阶贝塞尔曲线B(t) = (1 - t)^2 * P0 + 2t * (1 - t) * P1 + t^2 * P2, t ∈ [0,1]
     *
     * @param t  曲线长度比例
     * @param p0 起始点
     * @param p1 控制点
     * @param p2 终止点
     * @return t对应的点
     */
    public static PointF CalculateBezierPointForQuadratic(float t, PointF p0, PointF p1, PointF p2) {
        PointF point = new PointF();
        float temp = 1 - t;
        float tPow2 = (float) Math.pow(t, 2);
        float tempPow2 = (float) Math.pow(temp, 2);
        point.x = tempPow2 * p0.x + 2 * t * temp * p1.x + tPow2 * p2.x;
        point.y = tempPow2 * p0.y + 2 * t * temp * p1.y + tPow2 * p2.y;
        return point;
    }

    /**
     * 三阶贝塞尔曲线B(t) = P0 * (1-t)^3 + 3 * P1 * t * (1-t)^2 + 3 * P2 * t^2 * (1-t) + P3 * t^3, t ∈ [0,1]
     *
     * @param t  曲线长度比例
     * @param p0 起始点
     * @param p1 控制点1
     * @param p2 控制点2
     * @param p3 终止点
     * @return t对应的点
     */
    public static PointF CalculateBezierPointForCubic(float t, PointF p0, PointF p1, PointF p2, PointF p3) {
        PointF point = new PointF();
        float temp = 1 - t;
        float tempPow3 = (float) Math.pow(temp, 3);
        float tempPow2 = (float) Math.pow(temp, 2);
        float tPow3 = (float) Math.pow(t, 3);
        float tPow2 = (float) Math.pow(t, 2);
        point.x = p0.x * tempPow3 + 3 * p1.x * t * tempPow2 + 3 * p2.x * tPow2 * temp + p3.x * tPow3;
        point.y = p0.y * tempPow3 + 3 * p1.y * t * tempPow2 + 3 * p2.y * tPow2 * temp + p3.y * tPow3;
        return point;
    }
}

