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

import zachg.gsctrainingandnutritiontracker.models.Message;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.ui.adapters.MessageListAdapter;
import zachg.gsctrainingandnutritiontracker.viewmodels.InboxViewModel;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class InboxFragment extends Fragment implements MessageListAdapter.OnItemClickListener {

    private RecyclerView mMsgRecyclerView;
    private InboxViewModel mInboxViewModel;
    private MessageListAdapter mMessageAdapter;
    private Message currentMessage = new Message();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public InboxFragment() {
        // required empty public constructor
    }

    static {
        FirebaseFirestore.setLoggingEnabled(true);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_inbox, container, false);

        mInboxViewModel = ViewModelProviders.of(getActivity()).get(InboxViewModel.class);

        mInboxViewModel.init();

        mInboxViewModel.getMessages().observe(this, new Observer<FirestoreRecyclerOptions<Message>>() {
            @Override
            public void onChanged(FirestoreRecyclerOptions<Message> messageOptions) {
                initRecyclerView(v, messageOptions);
                mMessageAdapter.startListening();
            }
        });

        mInboxViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (!aBoolean) {
                    mMsgRecyclerView.smoothScrollToPosition(mInboxViewModel.getMessages().getValue().getSnapshots().size() - 1);
                }
            }
        });

        return v;
    }

    public void initRecyclerView(View v, final FirestoreRecyclerOptions<Message> messages) {

        mMessageAdapter = new MessageListAdapter(messages);
        mMsgRecyclerView = v.findViewById(R.id.rv_inbox);
        mMsgRecyclerView.setHasFixedSize(true);
        mMsgRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMsgRecyclerView.setAdapter(mMessageAdapter);

        mMessageAdapter.setOnItemClickListener(new MessageListAdapter.OnItemClickListener() {
            // Fetch msg info into Msg object
            @Override
            public void onItemClick(DocumentSnapshot doc, int position) {
                getMsgAtPosition(position);
                String msgId = currentMessage.getId();

                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                            new MessageFragment()).addToBackStack(null).commit();
            }
        });
    }

    public Message getMsgAtPosition(int position) {
        Log.d(TAG, String.valueOf(currentMessage));
        return currentMessage;
    }

    @Override
    public void onStart() {
        super.onStart();
        //mMessageAdapter.startListening();
    }

    public void onStop() {
        super.onStop();
        mMessageAdapter.stopListening();
    }

    @Override
    public void onItemClick(DocumentSnapshot doc, int position) {
        // go to message fragment
    }
}