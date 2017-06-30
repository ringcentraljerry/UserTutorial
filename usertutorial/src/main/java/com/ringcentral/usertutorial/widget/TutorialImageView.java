package com.ringcentral.usertutorial.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.ringcentral.usertutorial.TargetViewClickListener;
import com.ringcentral.usertutorial.TutorialTargetViewInterface;

/**
 * @author Jerry Cai
 */
public class TutorialImageView extends android.support.v7.widget.AppCompatImageView implements TutorialTargetViewInterface {
    public TutorialImageView(Context context) {
        super(context);
    }

    public TutorialImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TutorialImageView(Context context, AttributeSet attrs, int defStyleAttr) {
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
