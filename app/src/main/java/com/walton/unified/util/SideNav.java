package com.walton.unified.util;

public abstract class SideNav {

    public static float evaluate(float fraction, float startValue, float endValue) {
        return startValue + fraction * (endValue - startValue);
    }
}
