package com.ringcentral.usertutorial.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.ringcentral.usertutorial.TargetViewContainerClickListener;

/**
 * @author Jerry Cai
 */
public class TutorialRelativeLayoutContainer extends RelativeLayout {
    public TutorialRelativeLayoutContainer(Context context) {
        super(context);
    }

    public TutorialRelativeLayoutContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TutorialRelativeLayoutContainer(Context context, AttributeSet attrs, int defStyleAttr) {
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
