package com.deadline.facebookLoading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.deadline.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by deadline on 2017/5/26.
 * 指定动画，指定形状的的动态颜色块
 */

public class LoadingShapeView extends View {

    public static final int SHAPE_TYPE_CUSTOM = 0;
    public static final int SHAPE_TYPE_CIRCLE = 1;
    public static final int SHAPE_TYPE_RECTANGLE = 2;
    public static final int SHAPE_TYPE_ROUND_RECTANGLE = 3;
    @IntDef({
            SHAPE_TYPE_CUSTOM,
            SHAPE_TYPE_CIRCLE,
            SHAPE_TYPE_RECTANGLE,
            SHAPE_TYPE_ROUND_RECTANGLE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ShapeType{

    }

    public static final int ANIMATION_TYPE_NONE = 0;
    public static final int ANIMATION_TYPE_SCALE = 1;
    public static final int ANIMATION_TYPE_LENGTH_SCALE_LEFT = 2;
    public static final int ANIMATION_TYPE_LENGTH_SCALE_RIGHT = 3;
    @IntDef({
            ANIMATION_TYPE_NONE,
            ANIMATION_TYPE_SCALE,
            ANIMATION_TYPE_LENGTH_SCALE_LEFT,
            ANIMATION_TYPE_LENGTH_SCALE_RIGHT
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationType{

    }

    private int width, height;
    private Paint paint;
    private int roundRectRadius;
    private RectF mRectF;
    private float friction;
    private ValueAnimator animator;
    private int animationDuration = 1000;
    private int color = 0xFFEAEAEA;
    private int sourceColor;
    private int shapeType = SHAPE_TYPE_CUSTOM;
    private int animationType = ANIMATION_TYPE_NONE;
    private OnCustomShapeDrawListener mDrawListener;
    private FrictionTransformer mFrictionTransformer;
    private ColorTransformer mColorTransformer;

    public LoadingShapeView(Context context) {
        this(context, null);
    }

    public LoadingShapeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingShapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LoadingShapeView);

        animationDuration = array.getInteger(R.styleable.LoadingShapeView_duration, animationDuration);
        color = array.getColor(R.styleable.LoadingShapeView_drawColor, color);
        shapeType = array.getInt(R.styleable.LoadingShapeView_shapeType, shapeType);
        animationType = array.getInt(R.styleable.LoadingShapeView_animType, animationType);
        array.recycle();
        setUp();
    }


    /**
     * 用于自定义形状的绘制
     */
    public interface OnCustomShapeDrawListener{

        void onDraw(Canvas canvas, Paint paint, RectF rectF, float friction);
    }

    private void setUp() {
        friction = 1.0f;
        sourceColor = color;
        roundRectRadius = (int)dip2px(3);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        mRectF = new RectF();
    }

    public void start() {
        stop();

        float[] values = mFrictionTransformer == null
                ? new float[]{0f, 1f, 0f}
                : mFrictionTransformer.getInitFloatValue();

        animator = ValueAnimator.ofFloat(values);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();

                if (value != friction) {
                    friction = value;
                    invalidate();
                }
            }
        });

        animator.setDuration(animationDuration);
        animator.start();
    }

    public void stop(){
        if(animator != null){
            animator.removeAllListeners();
            animator.cancel();
            animator = null;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        width = widthSize - getPaddingLeft() - getPaddingRight();
        height = heightSize - getPaddingTop() - getPaddingBottom();
        mRectF.left = getPaddingLeft();
        mRectF.right = width + getPaddingLeft();
        mRectF.top = getPaddingTop();
        mRectF.bottom = height + getPaddingTop();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        friction = mFrictionTransformer == null
                ? friction : mFrictionTransformer.transformer(friction);

        color = mColorTransformer == null
                ? color : mColorTransformer.transformer(sourceColor, mColorTransformer.transformerFriction(friction));
        paint.setColor(color);

        switch (shapeType){
            case SHAPE_TYPE_CUSTOM:
                if(mDrawListener != null){
                   mDrawListener.onDraw(canvas, paint, mRectF, friction);
                }
                break;
            case SHAPE_TYPE_CIRCLE:
                int radius = Math.min(width / 2, height / 2);
                canvas.drawCircle(width / 2, height / 2, radius * friction, paint);
                break;
            case SHAPE_TYPE_RECTANGLE:
                mRectF = animationType == ANIMATION_TYPE_LENGTH_SCALE_RIGHT ? scaleRight(friction, mRectF)
                        : animationType == ANIMATION_TYPE_LENGTH_SCALE_LEFT ? scaleLeft(friction, mRectF) : mRectF;
                canvas.drawRect(mRectF, paint);
                break;
            case SHAPE_TYPE_ROUND_RECTANGLE:
                mRectF = animationType == ANIMATION_TYPE_LENGTH_SCALE_RIGHT ? scaleRight(friction, mRectF)
                        : animationType == ANIMATION_TYPE_LENGTH_SCALE_LEFT ? scaleLeft(friction, mRectF) : mRectF;
                canvas.drawRoundRect(mRectF,
                        roundRectRadius, roundRectRadius, paint);
                break;
        }
    }

    private RectF scaleRight(float friction, RectF rectF){
        rectF.right = rectF.left + width * friction;
        return rectF;
    }

    private RectF scaleLeft(float friction, RectF rectF){
        rectF.left = rectF.right - width * friction;
        return rectF;
    }

    public int getAnimationDuration() {
        return animationDuration;
    }

    public void setAnimationDuration(@IntRange(from = 0) int animationDuration) {
        this.animationDuration = animationDuration;
    }

    public @ColorInt int getColor() {
        return color;
    }

    public void setColor(@ColorInt int color) {
        this.color = color;
    }

    public @ShapeType int getShapeType() {
        return shapeType;
    }

    public void setShapeType(@ShapeType int shapeType) {
        this.shapeType = shapeType;
    }

    public @AnimationType int getAnimationType() {
        return animationType;
    }

    public void setAnimationType(@AnimationType int animationType) {
        this.animationType = animationType;
    }

    public void setOnCustomShapeDrawListener(OnCustomShapeDrawListener listener){
        this.mDrawListener = listener;
    }

    public void setFrictionTransformer(FrictionTransformer transformer){
        this.mFrictionTransformer = transformer;
    }

    public void setColorTransformer(ColorTransformer transformer){
        this.mColorTransformer = transformer;
    }

    public int getRoundRectRadius() {
        return roundRectRadius;
    }

    public void setRoundRectRadius(@IntRange(from = 0) int roundRectRadius) {
        this.roundRectRadius = roundRectRadius;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        stop();
    }

    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
