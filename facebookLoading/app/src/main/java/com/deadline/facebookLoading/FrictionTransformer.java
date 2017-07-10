package com.deadline.facebookLoading;

/**
 * Created by deadline on 2017/6/16.
 * 用于处理动画变换
 */

public interface FrictionTransformer {

    float[] getInitFloatValue();

    float transformer(float friction);
}
