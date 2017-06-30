
package com.ringcentral.usertutorial;

import android.view.View;

/**
 * @author Jerry Cai
 */
interface AnimationInterface {
    void fadeIn(View target, long duration, AnimationStartListener listener);

    void fadeOut(View target, long duration, AnimationEndListener listener);

    interface AnimationStartListener {
        void onAnimationStart();
    }

    interface AnimationEndListener {
        void onAnimationEnd();
    }
}
