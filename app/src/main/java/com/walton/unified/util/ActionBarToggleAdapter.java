package com.walton.unified.util;

import android.content.Context;

import androidx.drawerlayout.widget.DrawerLayout;

import com.walton.unified.SlideLayout;

public class ActionBarToggleAdapter extends DrawerLayout {

    private SlideLayout adapter;

    public ActionBarToggleAdapter(Context context) {
        super(context);
    }

    @Override
    public void openDrawer(int gravity) {
        adapter.openMenu();
    }

    @Override
    public void closeDrawer(int gravity) {
        adapter.closeMenu();
    }

    @Override
    public boolean isDrawerVisible(int drawerGravity) {
        return !adapter.isMenuClosed();
    }

    @Override
    public int getDrawerLockMode(int edgeGravity) {
        if (adapter.isMenuLocked() && adapter.isMenuClosed()) {
            return DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        } else if (adapter.isMenuLocked() && !adapter.isMenuClosed()) {
            return DrawerLayout.LOCK_MODE_LOCKED_OPEN;
        } else {
            return DrawerLayout.LOCK_MODE_UNLOCKED;
        }
    }

    public void setAdapter(SlideLayout adapter) {
        this.adapter = adapter;
    }
}
