package com.deadline.facebookLoading;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.deadline.R;

/**
 * Created by deadline on 2017/5/26.
 * 类似FaceBook的加载效果
 */

public class FacebookLoadingMoreView extends RelativeLayout{

    LoadingShapeView circle;

    public FacebookLoadingMoreView(Context context) {
        this(context, null);
    }

    public FacebookLoadingMoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FacebookLoadingMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUp();
    }

    private void setUp() {

        final View rootView = View.inflate(getContext(), R.layout.facebook_loading_more, this);
        circle = (LoadingShapeView) rootView.findViewById(R.id.circle);
        circle.setFrictionTransformer(new FrictionTransformer() {
            @Override
            public float[] getInitFloatValue() {
                return new float[]{0.5f, 1f, 0.5f};
            }

            @Override
            public float transformer(float friction) {
                return friction;
            }
        });

        circle.setColorTransformer(new GradientColorTransformer(Color.GREEN) {
            @Override
            public float transformerFriction(float friction) {
                return friction;
            }
        });
        circle.start();

        LoadingShapeView r1 = (LoadingShapeView) rootView.findViewById(R.id.round_rectangle_1);
        r1.setShapeType(LoadingShapeView.SHAPE_TYPE_RECTANGLE);
        r1.setAnimationType(LoadingShapeView.ANIMATION_TYPE_LENGTH_SCALE_RIGHT);
        r1.setFrictionTransformer(new FrictionTransformer() {
            @Override
            public float[] getInitFloatValue() {
                return new float[]{0.5f, 1f, 0.5f};
            }

            @Override
            public float transformer(float friction) {
                return friction;
            }
        });

        r1.start();

        LoadingShapeView r2 = (LoadingShapeView) rootView.findViewById(R.id.round_rectangle_2);
        r2.setShapeType(LoadingShapeView.SHAPE_TYPE_ROUND_RECTANGLE);
        r2.setAnimationType(LoadingShapeView.ANIMATION_TYPE_LENGTH_SCALE_RIGHT);
        r2.setFrictionTransformer(new FrictionTransformer() {
            @Override
            public float[] getInitFloatValue() {
                return new float[]{0.5f, 0.25f, 0.5f};
            }

            @Override
            public float transformer(float friction) {
                return friction;
            }
        });

        r2.start();
    }

}
