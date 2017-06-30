package com.ringcentral.usertutorial;

import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * @author Jerry Cai
 */
public class PopupItem extends Tip {
    private View mTargetView;
    @LayoutRes
    private int mContentLayout;
    private int mPosition;

    public PopupItem(int position, View targetView, int contentLayout) {
        mPosition = position;
        mTargetView = targetView;
        mContentLayout = contentLayout;
    }

    public void setTip(Tip tip) {
        this.setId(tip.getId());
        this.setViewId(tip.getViewId());
        this.setScreen(tip.getScreen());
        this.setText(tip.getText());
        this.setBrand(tip.getBrand());
        this.setPermission(tip.getPermission());
        this.setHotPadding(tip.getHotPadding());
        this.setHotZoomIn(tip.isHotZoomIn());
        this.setEventName(tip.getEventName());
        this.setFragment(tip.isFragment());
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }

    public View getTargetView() {
        return mTargetView;
    }

    @LayoutRes
    public int getContentLayout() {
        return mContentLayout;
    }

    @Override
    public int hashCode() {
        return 42;
    }

    @Override
    public boolean equals(Object o) {
        boolean isEqual = false;
        do {
            if (o == null) {
                break;
            }

            if (!(o instanceof PopupItem)) {
                break;
            }

            PopupItem t = (PopupItem) o;
            if (t.getId() != this.getId()) {
                break;
            }

            isEqual = true;

        } while (false);

        return isEqual;
    }
}
