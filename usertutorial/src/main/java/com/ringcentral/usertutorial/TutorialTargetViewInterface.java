package com.ringcentral.usertutorial;

import android.view.View;

/**
 * @author Jerry Cai
 */
public interface TutorialTargetViewInterface {
    TargetViewClickListener getTargetViewClickListener(View.OnClickListener l);
}
