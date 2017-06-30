package com.ringcentral.usertutorial.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.ringcentral.usertutorial.TargetViewClickListener;
import com.ringcentral.usertutorial.TutorialTargetViewInterface;


/**
 * @author Jerry Cai
 */
public class TutorialLinearLayout extends LinearLayout implements TutorialTargetViewInterface {
    public TutorialLinearLayout(Context context) {
        super(context);
    }

    public TutorialLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TutorialLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(getTargetViewClickListener(l));
    }

    @Override
    public TargetViewClickListener getTargetViewClickListener(OnClickListener l) {
        return new TargetViewClickListener(l);
    }
}
