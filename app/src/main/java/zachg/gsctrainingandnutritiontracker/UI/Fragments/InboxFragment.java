package zachg.gsctrainingandnutritiontracker.UI.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.Models.Message;
import zachg.gsctrainingandnutritiontracker.Models.User;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.Repositories.FirestoreRepository;
import zachg.gsctrainingandnutritiontracker.UI.Activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.UI.Adapters.MessageListAdapter;
import zachg.gsctrainingandnutritiontracker.ViewModels.AdminListViewModel;
import zachg.gsctrainingandnutritiontracker.ViewModels.MessageViewModel;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static zachg.gsctrainingandnutritiontracker.Repositories.FirestoreRepository.sMessages;

public class InboxFragment extends Fragment implements MessageListAdapter.OnItemClickListener {

    private RecyclerView mMsgRecyclerView;
    private MessageViewModel mMessageViewModel;
    private MessageListAdapter mMessageAdapter;
    static Message currentSelectedMessage = new Message();
    private FirestoreRepository mRepo = new FirestoreRepository();

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
        View v = inflater.inflate(R.layout.fragment_inbox, container, false);

        FirestoreRecyclerOptions<Message> mMessageOptions = mRepo.setMessages();
//        mMessageViewModel = ViewModelProviders.of(getActivity()).get(MessageViewModel.class);
//        mMessageViewModel.init();

        mMessageAdapter = new MessageListAdapter(mMessageOptions);

        mMsgRecyclerView = v.findViewById(R.id.rv_inbox);
        mMsgRecyclerView.setHasFixedSize(true);
        mMsgRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMsgRecyclerView.setAdapter(mMessageAdapter);

        mMessageAdapter.setOnItemClickListener(new MessageListAdapter.OnItemClickListener() {
            // Fetch msg info into Msg object
            @Override
            public void onItemClick(DocumentSnapshot doc, int position) {
                getMsgAtPosition(position);
                String msgId = currentSelectedMessage.getId();

                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                            new MessageFragment()).addToBackStack(null).commit();
            }
        });
        return v;
    }

    public Message getMsgAtPosition(int position) {
        currentSelectedMessage = sMessages.get(position);
        Log.d(TAG, String.valueOf(currentSelectedMessage));
        return currentSelectedMessage;
    }

    @Override
    public void onStart() {
        super.onStart();
        mMessageAdapter.startListening();
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
