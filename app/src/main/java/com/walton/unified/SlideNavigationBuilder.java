package com.walton.unified;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import com.walton.unified.transform.CompositeTransformation;
import com.walton.unified.transform.ElevationTransformation;
import com.walton.unified.transform.RootTransformation;
import com.walton.unified.transform.ScaleTransformation;
import com.walton.unified.transform.TranslationTransformation;
import com.walton.unified.util.ActionBarToggleAdapter;
import com.walton.unified.util.DrawerListenerAdapter;
import com.walton.unified.util.Menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlideNavigationBuilder {

    private static final float DEFAULT_END_SCALE = 0.65f;
    private static final int DEFAULT_END_ELEVATION_DP = 8;
    private static final int DEFAULT_DRAG_DIST_DP = 180;

    private Activity activity;

    private ViewGroup contentView;

    private View menuView;
    private int menuLayoutRes;

    private List<RootTransformation> transformations;

    private List<DragListener> dragListeners;

    private List<DragStateListener> dragStateListeners;

    private int dragDistance;

    private Toolbar toolbar;

    private SlideGravity gravity;

    private boolean isMenuOpened;

    private boolean isMenuLocked;

    private boolean isContentClickableWhenMenuOpened;

    private Bundle savedState;

    public SlideNavigationBuilder(Activity activity) {
        this.activity = activity;
        this.transformations = new ArrayList<>();
        this.dragListeners = new ArrayList<>();
        this.dragStateListeners = new ArrayList<>();
        this.gravity = SlideGravity.LEFT;
        this.dragDistance = dpToPx(DEFAULT_DRAG_DIST_DP);
        this.isContentClickableWhenMenuOpened = true;
    }

    public SlideNavigationBuilder withMenuView(View view) {
        menuView = view;
        return this;
    }

    public SlideNavigationBuilder withMenuLayout(@LayoutRes int layout) {
        menuLayoutRes = layout;
        return this;
    }

    public SlideNavigationBuilder withToolbarMenuToggle(Toolbar tb) {
        toolbar = tb;
        return this;
    }

    public SlideNavigationBuilder withGravity(SlideGravity g) {
        gravity = g;
        return this;
    }

    public SlideNavigationBuilder withContentView(ViewGroup cv) {
        contentView = cv;
        return this;
    }

    public SlideNavigationBuilder withMenuLocked(boolean locked) {
        isMenuLocked = locked;
        return this;
    }

    public SlideNavigationBuilder withSavedState(Bundle state) {
        savedState = state;
        return this;
    }

    public SlideNavigationBuilder withMenuOpened(boolean opened) {
        isMenuOpened = opened;
        return this;
    }

    public SlideNavigationBuilder withContentClickableWhenMenuOpened(boolean clickable) {
        isContentClickableWhenMenuOpened = clickable;
        return this;
    }

    public SlideNavigationBuilder withDragDistance(int dp) {
        return withDragDistancePx(dpToPx(dp));
    }

    public SlideNavigationBuilder withDragDistancePx(int px) {
        dragDistance = px;
        return this;
    }

    public SlideNavigationBuilder withRootViewScale(@FloatRange(from = 0.01f) float scale) {
        transformations.add(new ScaleTransformation(scale));
        return this;
    }

    public SlideNavigationBuilder withRootViewElevation(@IntRange(from = 0) int elevation) {
        return withRootViewElevationPx(dpToPx(elevation));
    }

    public SlideNavigationBuilder withRootViewElevationPx(@IntRange(from = 0) int elevation) {
        transformations.add(new ElevationTransformation(elevation));
        return this;
    }

    public SlideNavigationBuilder withRootViewYTranslation(int translation) {
        return withRootViewYTranslationPx(dpToPx(translation));
    }

    public SlideNavigationBuilder withRootViewYTranslationPx(int translation) {
        transformations.add(new TranslationTransformation(translation));
        return this;
    }

    public SlideNavigationBuilder addRootTransformation(RootTransformation transformation) {
        transformations.add(transformation);
        return this;
    }

    public SlideNavigationBuilder addDragListener(DragListener dragListener) {
        dragListeners.add(dragListener);
        return this;
    }

    public SlideNavigationBuilder addDragStateListener(DragStateListener dragStateListener) {
        dragStateListeners.add(dragStateListener);
        return this;
    }

    public SlidingRootNav inject() {
        ViewGroup contentView = getContentView();

        View oldRoot = contentView.getChildAt(0);
        contentView.removeAllViews();

        SlideLayout newRoot = createAndInitNewRoot(oldRoot);

        View menu = getMenuViewFor(newRoot);

        initToolbarMenuVisibilityToggle(newRoot, menu);

        Menu clickConsumer = new Menu(activity);
        clickConsumer.setMenuHost(newRoot);

        newRoot.addView(menu);
        newRoot.addView(clickConsumer);
        newRoot.addView(oldRoot);

        contentView.addView(newRoot);

        if (savedState == null) {
            if (isMenuOpened) {
                newRoot.openMenu(false);
            }
        }

        newRoot.setMenuLocked(isMenuLocked);

        return newRoot;
    }

    private SlideLayout createAndInitNewRoot(View oldRoot) {
        SlideLayout newRoot = new SlideLayout(activity);
        newRoot.setId(R.id.srn_root_layout);
        newRoot.setRootTransformation(createCompositeTransformation());
        newRoot.setMaxDragDistance(dragDistance);
        newRoot.setGravity(gravity);
        newRoot.setRootView(oldRoot);
        newRoot.setContentClickableWhenMenuOpened(isContentClickableWhenMenuOpened);
        for (DragListener l : dragListeners) {
            newRoot.addDragListener(l);
        }
        for (DragStateListener l : dragStateListeners) {
            newRoot.addDragStateListener(l);
        }
        return newRoot;
    }

    private ViewGroup getContentView() {
        if (contentView == null) {
            contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        }
        return contentView;
    }

    private View getMenuViewFor(SlideLayout parent) {
        if (menuView == null) {
            menuView = LayoutInflater.from(activity).inflate(menuLayoutRes, parent, false);
        }
        return menuView;
    }

    private RootTransformation createCompositeTransformation() {
        if (transformations.isEmpty()) {
            return new CompositeTransformation(Arrays.asList(
                    new ScaleTransformation(DEFAULT_END_SCALE),
                    new ElevationTransformation(dpToPx(DEFAULT_END_ELEVATION_DP))));
        } else {
            return new CompositeTransformation(transformations);
        }
    }

    protected void initToolbarMenuVisibilityToggle(final SlideLayout sideNav, View drawer) {
        if (toolbar != null) {
            ActionBarToggleAdapter dlAdapter = new ActionBarToggleAdapter(activity);
            dlAdapter.setAdapter(sideNav);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(activity, dlAdapter, toolbar,
                    R.string.srn_drawer_open,
                    R.string.srn_drawer_close);
            toggle.syncState();
            DrawerListenerAdapter listenerAdapter = new DrawerListenerAdapter(toggle, drawer);
            sideNav.addDragListener(listenerAdapter);
            sideNav.addDragStateListener(listenerAdapter);
        }
    }

    private int dpToPx(int dp) {
        return Math.round(activity.getResources().getDisplayMetrics().density * dp);
    }

}
