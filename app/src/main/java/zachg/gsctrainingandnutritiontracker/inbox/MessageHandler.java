package zachg.gsctrainingandnutritiontracker.inbox;

import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MessageHandler {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static final CollectionReference msgColRef = db.collection("messages");

    public static ArrayList<Message> fetchMsgs(final ArrayList<Message> mMsgs) {

        Query msgQuery = msgColRef;
        getMsgOptions(msgColRef);

        msgQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Message msg = new Message();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        msg = doc.toObject(Message.class);
                        mMsgs.add(msg);
                    }
                } else {
                    Log.e(TAG, "failed to make mMsgs");
                }
            }
        });
        return mMsgs;
    }

    public static FirestoreRecyclerOptions<Message> getMsgOptions(Query query) {
        FirestoreRecyclerOptions<Message> msgOptions = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();

        return msgOptions;
    }
}