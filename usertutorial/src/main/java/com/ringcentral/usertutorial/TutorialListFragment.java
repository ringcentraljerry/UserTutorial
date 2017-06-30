package com.ringcentral.usertutorial;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

import com.ringcentral.usertutorial.activity.TutorialActivityInterface;

/**
 * @author Jerry Cai
 */
public class TutorialListFragment extends ListFragment {
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.getActivity() instanceof TutorialActivityInterface) {
            TutorialActivityInterface tai = (TutorialActivityInterface) this.getActivity();
            tai.onViewRootResume();
        }
    }
}
