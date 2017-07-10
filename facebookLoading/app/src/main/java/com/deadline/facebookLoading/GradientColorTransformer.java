package com.deadline.facebookLoading;

/**
 * Created by deadline on 2017/6/16.
 * 颜色渐变
 */

public abstract class GradientColorTransformer implements ColorTransformer{

    private int toColor;

    public GradientColorTransformer(int color){
        this.toColor = color;
    }

    public void setTransformColor(int color){
        this.toColor = color;
    }

    @Override
    public int transformer(int color, float friction) {
        return (int)evaluate(friction, color, toColor);
    }

    public Object evaluate(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (int)((startA + (int)(fraction * (endA - startA))) << 24) |
                (int)((startR + (int)(fraction * (endR - startR))) << 16) |
                (int)((startG + (int)(fraction * (endG - startG))) << 8) |
                (int)((startB + (int)(fraction * (endB - startB))));
    }
}
