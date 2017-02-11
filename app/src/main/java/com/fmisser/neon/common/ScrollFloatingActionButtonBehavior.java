package com.fmisser.neon.common;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * Date: 2017/2/8.
 * Author: fmisser
 * Description: fab hide or show when scroll
 */

public abstract class ScrollFloatingActionButtonBehavior extends FloatingActionButton.Behavior {

    private boolean mVisible = true;

    public ScrollFloatingActionButtonBehavior() {
        super();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (dyConsumed > 0 && mVisible) {
            onHide();
            mVisible = false;
        } else if (dyConsumed < 0 && !mVisible) {
            onShow();
            mVisible = true;
        }
    }

    public abstract void onHide();
    public abstract void onShow();
}
