package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import zachg.gsctrainingandnutritiontracker.models.Message;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class InboxViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo;
    private MutableLiveData<FirestoreRecyclerOptions<Message>> messagesLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private User currentUser = new User();
    private Message message = new Message();
    private List messageList;
    public String TAG = "InboxVM";

    public void init(User user) {
        this.currentUser = user;
        repo = FirestoreRepository.getInstance();
        repo.getMessagesFromRepo(currentUser);
    }

    public MutableLiveData<FirestoreRecyclerOptions<Message>> getMessages() {
        return messagesLiveData;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot doc : task.getResult()) {
                message = doc.toObject(Message.class);
                // Add to list
                messageList.add(message);
                messagesLiveData.setValue();
                Log.d(TAG, currentUser.getClientName() + " message #: " + String.valueOf(messageList.size()));
            }
        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
        }
    }

    // TODO: onComplete
    public Message onItemClicked(DocumentSnapshot documentSnapshot, int position) {
        // Fetches currentMessage
        message = documentSnapshot.toObject(Message.class);
        return message;
    }
}