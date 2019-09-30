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

public class InboxFragment extends Fragment implements MessageListAdapter.OnItemClickListener {

    private FragmentInboxBinding binding;
    private RecyclerView messageRV;
    private InboxViewModel inboxViewModel;
    private MessageListAdapter messageAdapter;
    private User currentUser = new User();
    private User user = new User();
    private Message currentMessage = new Message();

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public InboxFragment() {
        // required empty public constructor
    }

    public InboxFragment(User user) {
        this.currentUser = user;
    }

    static {
        FirebaseFirestore.setLoggingEnabled(true);
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
            public void onChanged(FirestoreRecyclerOptions<Message> messageOptions) {
                initRecyclerView(v, messageOptions);
                messageAdapter.startListening();
            }
        });

        inboxViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (!aBoolean) {
                    //msgRecyclerView.smoothScrollToPosition(inboxViewModel.getMessages().getValue().getSnapshots().size() - 1);
                }
            }
        });

        return v;
    }

    public void initRecyclerView(View v, final FirestoreRecyclerOptions<Message> messages) {

        messageAdapter = new MessageListAdapter(messages);
        //binding.setMessageAdapter(messageAdapter);

        messageAdapter.setOnItemClickListener(new MessageListAdapter.OnItemClickListener() {
            // Fetch msg info into Msg object
            @Override
            public void onItemClick(DocumentSnapshot doc, int position) {
                currentMessage = messageAdapter.getMessageAtPosition(messages.getSnapshots().get(position));
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                            new MessageFragment(currentMessage, user)).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public void onStart() { super.onStart(); }

    public void onStop() {
        super.onStop();
        messageAdapter.stopListening();
    }

    @Override
    public void onItemClick(DocumentSnapshot doc, int position) {
        // go to message fragment
    }
}