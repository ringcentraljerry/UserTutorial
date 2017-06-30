package com.ringcentral.usertutorial.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.ringcentral.usertutorial.TargetViewContainerClickListener;


/**
 * @author Jerry Cai
 */
public class TutorialLinearLayoutContainer extends LinearLayout {
    public TutorialLinearLayoutContainer(Context context) {
        super(context);
    }

    public TutorialLinearLayoutContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TutorialLinearLayoutContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(new TargetViewContainerClickListener(l));
    }

    @Override
    public void setVisibility(int visibility) {
        for(int i = 0; i< this.getChildCount(); ++i) {
            this.getChildAt(i).setVisibility(visibility);
        }
        super.setVisibility(visibility);
    }
}
