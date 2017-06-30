package com.ringcentral.usertutorial;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Jerry Cai
 */
public class TargetViewContainerClickListener implements View.OnClickListener {
    private View.OnClickListener mClickListener;

    public TargetViewContainerClickListener(View.OnClickListener listener) {
        mClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if ((v instanceof ViewGroup) && (v.getContext() instanceof TipTouchEventInterface)) {
            ViewGroup container = (ViewGroup) v;
            TipTouchEventInterface tutorialAct = (TipTouchEventInterface) v.getContext();
            final Resources res = v.getContext().getResources();
            for (int i = 0; i < container.getChildCount(); ++i) {
                View item = container.getChildAt(i);
                if (item instanceof TutorialTargetViewInterface) {
                    String fullTargetViewName = res.getResourceName(item.getId());
                    int start = fullTargetViewName.lastIndexOf("/");
                    String targetViewName = fullTargetViewName.substring(start + 1);
                    tutorialAct.onTipClickEvent(targetViewName);
                }
            }
        }
        mClickListener.onClick(v);
    }
}
