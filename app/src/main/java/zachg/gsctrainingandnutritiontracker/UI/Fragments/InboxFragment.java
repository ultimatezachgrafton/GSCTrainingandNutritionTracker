package zachg.gsctrainingandnutritiontracker.UI.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.Models.Message;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.UI.Activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.ViewModels.MessageHandler;
import zachg.gsctrainingandnutritiontracker.UI.Adapters.MessageListAdapter;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class InboxFragment extends Fragment implements MessageListAdapter.OnItemClickListener {

    private RecyclerView mMsgRecyclerView;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final CollectionReference msgCol = db.collection("messages");
    private MessageListAdapter adapter;
    private static ArrayList<Message> mMsgs = new ArrayList<>();
    private static Message currentSelectedMsg;

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

        // fetch Msgs for display
        MessageHandler.fetchMsgs(mMsgs);

        adapter = new MessageListAdapter(MessageHandler.getMsgOptions(MessageHandler.msgColRef));

        mMsgRecyclerView = v.findViewById(R.id.rv_inbox);
        mMsgRecyclerView.setHasFixedSize(true);
        mMsgRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMsgRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MessageListAdapter.OnItemClickListener() {
            // Fetch msg info into Msg object
            @Override
            public void onItemClick(DocumentSnapshot doc, int position) {
                getMsgAtPosition(position);
                String msgId = currentSelectedMsg.getId();

                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                            new MessageFragment()).addToBackStack(null).commit();
            }
        });
        return v;
    }

    public Message getMsgAtPosition(int position) {
        currentSelectedMsg = mMsgs.get(position);
        Log.d(TAG, String.valueOf(currentSelectedMsg));
        return currentSelectedMsg;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onItemClick(DocumentSnapshot doc, int position) {
        // go to message fragment
    }
}
