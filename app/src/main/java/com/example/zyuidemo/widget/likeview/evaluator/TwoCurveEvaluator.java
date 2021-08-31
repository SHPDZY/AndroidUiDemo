package com.example.zyuidemo.widget.likeview.evaluator;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

import com.example.libcommon.utils.BezierUtil;

/**
 * @Description: 二阶贝赛尔曲线
 */
public class TwoCurveEvaluator implements TypeEvaluator<PointF> {

    private final PointF mControlP1;

    public TwoCurveEvaluator(PointF pointF1) {
        this.mControlP1 = pointF1;
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        // 三阶贝赛尔曲线
        return BezierUtil.CalculateBezierPointForQuadratic(fraction,startValue,mControlP1,endValue);
    }
}