package com.walton.unified.util;

import android.view.View;

import androidx.drawerlayout.widget.DrawerLayout;

import com.walton.unified.DragListener;
import com.walton.unified.DragStateListener;


public class DrawerListenerAdapter implements DragListener, DragStateListener {

    private DrawerLayout.DrawerListener adapter;
    private View drawer;

    public DrawerListenerAdapter(DrawerLayout.DrawerListener adapter, View drawer) {
        this.adapter = adapter;
        this.drawer = drawer;
    }

    @Override
    public void onDrag(float progress) {
        adapter.onDrawerSlide(drawer, progress);
    }

    @Override
    public void onDragStart() {
        adapter.onDrawerStateChanged(DrawerLayout.STATE_DRAGGING);
    }

    @Override
    public void onDragEnd(boolean isMenuOpened) {
        if (isMenuOpened) {
            adapter.onDrawerOpened(drawer);
        } else {
            adapter.onDrawerClosed(drawer);
        }
        adapter.onDrawerStateChanged(DrawerLayout.STATE_IDLE);
    }
}
