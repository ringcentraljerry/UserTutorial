package com.ringcentral.usertutorial.widget;

import android.content.Context;
import android.util.AttributeSet;
import com.ringcentral.usertutorial.TargetViewClickListener;
import com.ringcentral.usertutorial.TutorialTargetViewInterface;


/**
 * @author Jerry Cai
 */
public class TutorialButton extends android.support.v7.widget.AppCompatButton implements TutorialTargetViewInterface {
    public TutorialButton(Context context) {
        super(context);
    }

    public TutorialButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TutorialButton(Context context, AttributeSet attrs, int defStyleAttr) {
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
