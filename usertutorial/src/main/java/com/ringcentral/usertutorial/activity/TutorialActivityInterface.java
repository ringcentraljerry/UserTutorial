package com.ringcentral.usertutorial.activity;

import android.view.ViewGroup;

/**
 * @author Jerry Cai
 */
public interface TutorialActivityInterface {
    ViewGroup getViewRootForTip();

    void onViewRootResume();

    void onViewRootPause();

    boolean isTipShowing();
}
