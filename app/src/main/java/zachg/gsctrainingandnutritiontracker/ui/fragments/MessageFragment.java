package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentMsgBinding;
import zachg.gsctrainingandnutritiontracker.models.Message;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.MessageViewModel;

public class MessageFragment extends DialogFragment {

    private FragmentMsgBinding binding;
    private Message currentMessage;
    private String clientName, title, body, date;
    public MessageViewModel messageViewModel;
    private User user;

    public MessageFragment() {
        // empty message to be populated
        // TODO: if Admin, you may change the recipient name
        // on click, pull up AdminUserList - click a name -
    }

    public MessageFragment(final Message message, final User sender) {
        messageViewModel = new MessageViewModel(message);
        this.currentMessage = message;
        this.clientName = currentMessage.getClientName();
        this.date = currentMessage.getDate();
        this.title = currentMessage.getTitle();
        this.body = currentMessage.getBody();
        // reply text body
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMsgBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();
        binding.setMessage(currentMessage);

        messageViewModel = ViewModelProviders.of(getActivity()).get(MessageViewModel.class);

        return v;
    }

    public void onReplyClicked() {
        // onSendClicked
        // send Message to original client
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new MessageFragment()).addToBackStack(null).commit();
    }
}
