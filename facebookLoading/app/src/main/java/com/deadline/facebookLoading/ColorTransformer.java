package com.deadline.facebookLoading;

/**
 * Created by deadline on 2017/6/16.
 * 用于处理动画期间的颜色渐变
 */

public interface ColorTransformer {

    int transformer(int color, float friction);

    float transformerFriction(float friction);
}
