package zachg.gsctrainingandnutritiontracker.UI.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.UI.Activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.utils.OnSwipeTouchListener;

public class MessageFragment extends DialogFragment {

    TextView tvClientName;
    TextView tvMsgTitle;
    TextView tvMsgBody;

    // swipe between msgs
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_msg, container, false);

        tvClientName = v.findViewById(R.id.tvClientName);
        tvMsgTitle = v.findViewById(R.id.tvMsgTitle);
        tvMsgBody = v.findViewById(R.id.tvMsgBody);

        // set client name
        // set msgtitle
        // set msgbody

        v.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeRight() {
                // go to next msg
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new MessageFragment()).addToBackStack(null).commit();
            }

            public void onSwipeLeft() {
                // go to prev msg
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new MessageFragment()).addToBackStack(null).commit();
            }
        });
        return v;
    }
}
