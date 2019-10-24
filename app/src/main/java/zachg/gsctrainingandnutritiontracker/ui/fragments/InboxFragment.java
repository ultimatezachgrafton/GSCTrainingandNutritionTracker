package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import zachg.gsctrainingandnutritiontracker.databinding.FragmentInboxBinding;
import zachg.gsctrainingandnutritiontracker.models.Message;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.ui.adapters.MessageListAdapter;
import zachg.gsctrainingandnutritiontracker.ui.adapters.UserListAdapter;
import zachg.gsctrainingandnutritiontracker.viewmodels.InboxViewModel;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class InboxFragment extends Fragment {

    private FragmentInboxBinding binding;
    private InboxViewModel inboxViewModel;
    private MessageListAdapter messageAdapter;
    private User currentUser = new User();
    private User user = new User();
    private Message currentMessage = new Message();

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public InboxFragment() {}

    public InboxFragment(User user) {
        this.currentUser = user;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInboxBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();

        inboxViewModel = ViewModelProviders.of(getActivity()).get(InboxViewModel.class);

        inboxViewModel.init();

        inboxViewModel.getMessages().observe(this, new Observer<FirestoreRecyclerOptions<Message>>() {
            @Override
            public void onChanged(FirestoreRecyclerOptions<Message> messages) {
                initRecyclerView(messages);
                messageAdapter.startListening();
            }
        });

        inboxViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (!aBoolean) {
                    binding.rvInbox.smoothScrollToPosition(inboxViewModel.getMessages().getValue().getSnapshots().size() - 1);
                }
            }
        });

        return v;
    }

    public void initRecyclerView(FirestoreRecyclerOptions<Message> messages) {
        messageAdapter = new MessageListAdapter(messages);
        binding.rvInbox.setAdapter(messageAdapter);

        binding.rvInbox.setHasFixedSize(true);
        binding.rvInbox.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() { super.onStart(); }

    public void onStop() {
        super.onStop();
        messageAdapter.stopListening();
    }

    public void onItemClick(Message message, User user) {
        // go to Message fragment
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new MessageFragment(message, user)).addToBackStack(null).commit();
    }
}