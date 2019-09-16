package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import zachg.gsctrainingandnutritiontracker.databinding.FragmentMsgBinding;
import zachg.gsctrainingandnutritiontracker.models.Message;
import zachg.gsctrainingandnutritiontracker.viewmodels.MessageViewModel;

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

        return v;
    }
}
