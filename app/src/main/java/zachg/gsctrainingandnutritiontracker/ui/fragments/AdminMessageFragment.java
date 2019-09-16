package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import zachg.gsctrainingandnutritiontracker.databinding.FragmentMsgBinding;
import zachg.gsctrainingandnutritiontracker.models.Message;
import zachg.gsctrainingandnutritiontracker.viewmodels.AdminMessageViewModel;
import zachg.gsctrainingandnutritiontracker.viewmodels.MessageViewModel;

public class AdminMessageFragment extends DialogFragment {

    private FragmentMsgBinding binding;
    private Message currentMessage;
    private String clientName, title, body, date;
    public AdminMessageViewModel adminMessageViewModel;

    public AdminMessageFragment(Message message) {
        adminMessageViewModel = new AdminMessageViewModel(message);
        this.currentMessage = message;
        this.clientName = currentMessage.getClientName();
        this.date = currentMessage.getDate();
        this.title = currentMessage.getTitle();
        this.body = currentMessage.getBody();
    }

    // insert To: and he selects from registered users
    // reply function here and in regular messages

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMsgBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();
        binding.setMessage(currentMessage);

        adminMessageViewModel = ViewModelProviders.of(getActivity()).get(AdminMessageViewModel.class);

        return v;
    }
}
