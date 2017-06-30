package com.ringcentral.usertutorial;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.app.Fragment;

/**
 * @author Jerry Cai
 */
public class TipTimerPresenter extends TipPresenter {
    private static final int NEXT_TIME_TO_SHOW_TIP = 3 * 1000;
    private Handler mHandler = null;
    private TimerRunnable mTimerRunnable = null;

    public TipTimerPresenter(Activity target) {
        super(target);
        mHandler = new Handler();
        mTimerRunnable = new TimerRunnable();
    }

    @Override
    public void onHandleFinish() {
        //unimplemented.
    }

    //implement TipPresenter
    @Override
    public void onHandleResume() {
        getTipManager().show();
        startTimerToShowTip();
    }

    @Override
    public void onHandlePause() {
        getTipManager().hide();
        mHandler.removeCallbacks(mTimerRunnable);
    }

    @Override
    public boolean onHandleBackPressed() {
        startTimerToShowTip();
        return super.onHandleBackPressed();
    }

    @Override
    public void onHandleTouch() {
        startTimerToShowTip();
    }

    @Override
    public void setTargetFragment(Fragment targetFragment) {
        this.mTargetFragment = targetFragment;
        mTipManager.setTargetFragment(targetFragment);
    }

    //private methods
    private void startTimerToShowTip() {
        mHandler.removeCallbacks(mTimerRunnable);
        mHandler.postDelayed(mTimerRunnable, NEXT_TIME_TO_SHOW_TIP);
    }

    private class TimerRunnable implements Runnable {
        @Override
        public void run() {
            if (getTipManager().showTip()) {
                startTimerToShowTip();
            }
        }
    }
}
