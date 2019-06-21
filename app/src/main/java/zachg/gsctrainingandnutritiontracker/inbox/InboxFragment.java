package zachg.gsctrainingandnutritiontracker.inbox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.R;

public class InboxFragment extends Fragment implements MsgListAdapter.OnItemClickListener {

    private RecyclerView mMsgRecyclerView;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final CollectionReference msgCol = db.collection("messages");
    private MsgListAdapter adapter;
    private ArrayList<Msg> mMsgs;

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

        // Query the Firestore collection
        Query query = msgCol;
        // Build the database
        FirestoreRecyclerOptions<Msg> options = new FirestoreRecyclerOptions.Builder<Msg>()
                .setQuery(query, Msg.class)
                .build();

        adapter = new MsgListAdapter(options);

        mMsgRecyclerView = v.findViewById(R.id.rv_inbox);
        mMsgRecyclerView.setHasFixedSize(true);
        mMsgRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMsgRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MsgListAdapter.OnItemClickListener() {
            // Fetch msg info into Msg object
            @Override
            public void onItemClick(DocumentSnapshot doc, int position) {
                Msg msg = doc.toObject(Msg.class);
                String id = doc.getId();
                String path = doc.getReference().getPath();

                // Query for Reports with same firstName, lastName as user
                Query msgQuery = msgCol.whereEqualTo("date sent", msg.getMsgDate())
                        .whereEqualTo("client name", msg.getClientName())
                        .orderBy("unread", Query.Direction.ASCENDING)
                        .orderBy("date", Query.Direction.ASCENDING);
                msgQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc: task.getResult()) {
                                Msg msg = doc.toObject(Msg.class);
                                mMsgs.add(msg);
                            }
                        } else {
                            Toast.makeText(getActivity(), "failed to create mMsgs", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        return v;
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
