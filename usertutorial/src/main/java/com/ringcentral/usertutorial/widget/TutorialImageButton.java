package com.ringcentral.usertutorial.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.ringcentral.usertutorial.TargetViewClickListener;
import com.ringcentral.usertutorial.TutorialTargetViewInterface;

/**
 * @author Jerry Cai
 */
public class TutorialImageButton extends android.support.v7.widget.AppCompatImageButton implements TutorialTargetViewInterface {
    public TutorialImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TutorialImageButton(Context context) {
        super(context);
    }

    public TutorialImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
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
