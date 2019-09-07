package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.Date;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentClientProfileBinding;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentMsgBinding;
import zachg.gsctrainingandnutritiontracker.models.Message;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.ClientProfileViewModel;
import zachg.gsctrainingandnutritiontracker.viewmodels.MessageViewModel;
import zachg.gsctrainingandnutritiontracker.utils.OnSwipeTouchListener;

public class MessageFragment extends DialogFragment {

    private FragmentMsgBinding binding;
    private Message currentMessage;
    private String clientName, title, body, date;
    public MessageViewModel messageViewModel;

    public MessageFragment(Message message) {
        messageViewModel = new MessageViewModel(message);
        this.currentMessage = message;
        this.clientName = currentMessage.getClientName();
        this.date = currentMessage.getDate();
        this.title = currentMessage.getTitle();
        this.body = currentMessage.getBody();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMsgBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();
        binding.setMessage(currentMessage);

        messageViewModel = ViewModelProviders.of(getActivity()).get(MessageViewModel.class);

        v.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            public void onSwipeRight() {
                // go to next msg
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new MessageFragment(currentMessage)).addToBackStack(null).commit();
                Toast.makeText(getActivity(), "swiped", Toast.LENGTH_LONG).show();
            }
            public void onSwipeLeft() {
                // go to prev msg
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new MessageFragment(currentMessage)).addToBackStack(null).commit();
                Toast.makeText(getActivity(), "swiped", Toast.LENGTH_LONG).show();
            }
        });
        return v;
    }
}
