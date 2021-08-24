package com.walton.unified.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.walton.unified.SlideLayout;


public class Menu extends View {

    private SlideLayout menuHost;

    public Menu(Context context) {
        super(context);
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        return menuHost.isMenuClosed();
    }

    public void setMenuHost(SlideLayout layout) {
        this.menuHost = layout;
    }
}
