package com.ringcentral.usertutorial;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;

/**
 * @author Jerry Cai
 */
public class TargetViewClickListener implements View.OnClickListener {
    private View.OnClickListener mClickListener;

    public TargetViewClickListener(View.OnClickListener listener) {
        mClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        if (v.getId() > 0) {
            if (context instanceof TipTouchEventInterface) {
                try {
                    TipTouchEventInterface tutorialAct = (TipTouchEventInterface) context;
                    String fullTargetViewName = context.getResources().getResourceName(v.getId());
                    int start = fullTargetViewName.lastIndexOf("/");
                    String targetViewName = fullTargetViewName.substring(start + 1);
                    tutorialAct.onTipClickEvent(targetViewName);
                } catch (Resources.NotFoundException e){
                    Log.e("Tutorial","Resource name not found");
                }
            }
        }
        mClickListener.onClick(v);
    }
}
