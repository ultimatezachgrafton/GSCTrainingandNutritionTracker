package zachg.gsctrainingandnutritiontracker.UI.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import zachg.gsctrainingandnutritiontracker.Models.Message;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.UI.Activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.ViewModels.MessageViewModel;
import zachg.gsctrainingandnutritiontracker.ViewModels.ReportViewModel;
import zachg.gsctrainingandnutritiontracker.utils.OnSwipeTouchListener;

import static zachg.gsctrainingandnutritiontracker.UI.Fragments.InboxFragment.currentSelectedMessage;

public class MessageFragment extends DialogFragment {

    private MessageViewModel mMessageViewModel;
    TextView tvClientName;
    TextView tvMsgTitle;
    TextView tvMsgBody;

    // swipe between msgs
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_msg, container, false);

        mMessageViewModel = ViewModelProviders.of(getActivity()).get(MessageViewModel.class);
        mMessageViewModel.init();

        tvClientName = v.findViewById(R.id.tvClientName);
        tvMsgTitle = v.findViewById(R.id.tvMsgTitle);
        tvMsgBody = v.findViewById(R.id.tvMsgBody);

        tvClientName.setText(currentSelectedMessage.getClientName());
        tvMsgTitle.setText(currentSelectedMessage.getMsgTitle());
        tvMsgBody.setText(currentSelectedMessage.getMsgBody());

        v.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeRight() {
                // go to next msg
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new MessageFragment()).addToBackStack(null).commit();
                Toast.makeText(getActivity(), "swiped", Toast.LENGTH_LONG).show();
            }
            public void onSwipeLeft() {
                // go to prev msg
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new MessageFragment()).addToBackStack(null).commit();
                Toast.makeText(getActivity(), "swiped", Toast.LENGTH_LONG).show();
            }
        });
        return v;
    }
}
