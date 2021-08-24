package com.walton.unified;


public interface DragStateListener {

    void onDragStart();

    void onDragEnd(boolean isMenuOpened);
}
