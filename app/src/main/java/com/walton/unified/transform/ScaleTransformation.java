package com.walton.unified.transform;

import android.view.View;

import com.walton.unified.util.SideNav;

public class ScaleTransformation implements RootTransformation {

    private static final float START_SCALE = 1f;

    private final float endScale;

    public ScaleTransformation(float endScale) {
        this.endScale = endScale;
    }

    @Override
    public void transform(float dragProgress, View rootView) {
        float scale = SideNav.evaluate(dragProgress, START_SCALE, endScale);
        rootView.setScaleX(scale);
        rootView.setScaleY(scale);
    }
}
