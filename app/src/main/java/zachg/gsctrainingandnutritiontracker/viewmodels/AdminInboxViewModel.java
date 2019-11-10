package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import zachg.gsctrainingandnutritiontracker.models.Message;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AdminInboxViewModel extends ViewModel {

    private MutableLiveData<FirestoreRecyclerOptions<Message>> messages = new MutableLiveData<>();
    private FirestoreRepository repo;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private User currentUser = new User();
    private String TAG = "admininboxvm";

    public void init(User user) {
        this.currentUser = user;
        Log.d(TAG, currentUser.getEmail());
        if (messages.getValue() != null) {
            return;
        }
        repo = FirestoreRepository.getInstance();
        messages.setValue(repo.getMessagesFromRepo(currentUser));
    }

    public MutableLiveData<FirestoreRecyclerOptions<Message>> getMessages() {
        return messages;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }
}
