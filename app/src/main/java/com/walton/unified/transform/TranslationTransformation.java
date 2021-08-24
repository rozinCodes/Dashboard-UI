package com.walton.unified.transform;

import android.view.View;

import com.walton.unified.util.SideNav;

public class TranslationTransformation implements RootTransformation {

    private static final float START_TRANSLATION = 0f;

    private final float endTranslation;

    public TranslationTransformation(float endTranslation) {
        this.endTranslation = endTranslation;
    }

    @Override
    public void transform(float dragProgress, View rootView) {
        float translation = SideNav.evaluate(dragProgress, START_TRANSLATION, endTranslation);
        rootView.setTranslationY(translation);
    }
}
