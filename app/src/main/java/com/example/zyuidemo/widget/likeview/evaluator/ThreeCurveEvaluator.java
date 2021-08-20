package com.example.zyuidemo.widget.likeview.evaluator;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

import com.example.libcommon.utils.BezierUtil;

/**
 * @ClassName: ThreeCurveEvaluator
 * @Author: KaiSenGao
 * @CreateDate: 2020/12/24 10:58
 * @Description: 三阶贝赛尔曲线
 */
public class ThreeCurveEvaluator implements TypeEvaluator<PointF> {

    private final PointF mControlP1;
    private final PointF mControlP2;

    public ThreeCurveEvaluator(PointF pointF1, PointF pointF2) {
        this.mControlP1 = pointF1;
        this.mControlP2 = pointF2;
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        // 三阶贝赛尔曲线
        return BezierUtil.CalculateBezierPointForCubic(fraction,startValue,mControlP1,mControlP2,endValue);
    }
}