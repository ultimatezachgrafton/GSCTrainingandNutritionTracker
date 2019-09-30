package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentAdminMessageBinding;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentMsgBinding;
import zachg.gsctrainingandnutritiontracker.models.Message;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.AdminMessageViewModel;
import zachg.gsctrainingandnutritiontracker.viewmodels.MessageViewModel;

public class AdminMessageFragment extends DialogFragment {

    private FragmentAdminMessageBinding binding;
    private Message currentMessage;
    private String clientName, title, body, date;
    public AdminMessageViewModel adminMessageViewModel;
    public Button bReply, bUser;

    public AdminMessageFragment(final Message message, final User user) {
        adminMessageViewModel = new AdminMessageViewModel(message);
        this.currentMessage = message;
        this.clientName = currentMessage.getClientName();
        this.date = currentMessage.getDate();
        this.title = currentMessage.getTitle();
        this.body = currentMessage.getBody();

        bUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // bring up dialog fragment with RV user list
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new AdminMessageFragment(message, user)).addToBackStack(null).commit();
            }
        });

        bReply.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new AdminMessageFragment(message, user)).addToBackStack(null).commit();
            }
        });
    }

    // insert To: and he selects from registered users
    // reply function here and in regular messages

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminMessageBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();
        binding.setMessage(currentMessage);

        adminMessageViewModel = ViewModelProviders.of(getActivity()).get(AdminMessageViewModel.class);

        return v;
    }
}
