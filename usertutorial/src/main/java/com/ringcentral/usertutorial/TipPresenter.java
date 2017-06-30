package com.ringcentral.usertutorial;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * @author Jerry Cai
 */
public abstract class TipPresenter implements TipTouchEventInterface, TipViewEventTrace{
    protected TipManager mTipManager;
    protected Fragment mTargetFragment;

    public TipPresenter(Activity target) {
        mTipManager = new TipManager(target, this);
    }

    public TipManager getTipManager() {
        return mTipManager;
    }

    /**
     * when tap the target view. Flurry log
     *
     * @param targetViewName
     */
    @Override
    public void onTipClickEvent(String targetViewName) {
        mTipManager.markTip(targetViewName);
    }

    /**
     * when user tap close button. Flurry log
     */
    @Override
    public void onEventTapClose() {
        mTipManager.mark();
        mTipManager.remove();
    }

    /**
     * when user tap tip, Flurry log.
     */
    @Override
    public void onEventTapTip() {
        mTipManager.mark();
        //remove the view
        mTipManager.remove();
    }

    /**
     * when user tap to back. Flurry log
     *
     * @return
     */
    public boolean onHandleBackPressed() {
        boolean result = false;
        do {
            if (!mTipManager.hasTip()) {
                break;
            }

            if (!mTipManager.isShowing()) {
                break;
            }
            //mark
            mTipManager.mark();
            //remove the view
            mTipManager.remove();

            result = true;
        } while (false);

        return result;
    }

    public final void onDestroy() {
    }

    public boolean isShowing() {
        return mTipManager.isShowing();
    }

    /**
     * when tip is hidden
     */
    @Override
    public void onEventHide() {
    }

    /**
     * when tip is shown
     */
    @Override
    public void onEventShow() {
    }

    /**
     * when activity called finish() method.
     */
    public abstract void onHandleFinish();

    /**
     * when activity onResume.
     */
    public abstract void onHandleResume();

    /**
     * when activity onPause.
     */
    public abstract void onHandlePause();

    /**
     * when any touch event happens.
     */
    public abstract void onHandleTouch();

    public abstract void setTargetFragment(Fragment targetFragment);
}
