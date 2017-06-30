package com.ringcentral.usertutorial;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * @author Jerry Cai
 */
public class TipResumePresenter extends TipPresenter {
    public TipResumePresenter(Activity target) {
        super(target);
    }

    @Override
    public void onHandleFinish() {
        //unimplemented.
    }

    @Override
    public void onHandleResume() {
        if (!getTipManager().hasTip()) {
            return;
        }

        getTipManager().showTip();
    }

    @Override
    public void onHandlePause() {
        if (!getTipManager().hasTip()) {
            return;
        }

        getTipManager().hide();
    }

    @Override
    public boolean onHandleBackPressed() {
        return super.onHandleBackPressed();
    }

    @Override
    public void onHandleTouch() {
    }

    @Override
    public void setTargetFragment(Fragment targetFragment) {
        this.mTargetFragment = targetFragment;
        mTipManager.setTargetFragment(targetFragment);
    }
}
