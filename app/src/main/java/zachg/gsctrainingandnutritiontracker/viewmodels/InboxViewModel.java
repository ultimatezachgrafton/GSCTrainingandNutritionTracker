package zachg.gsctrainingandnutritiontracker.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import zachg.gsctrainingandnutritiontracker.models.Message;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class InboxViewModel extends ViewModel {

    private MutableLiveData<FirestoreRecyclerOptions<Message>> messages = new MutableLiveData<>();
    private FirestoreRepository repo;
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public void init() {
        if (messages.getValue() != null) {
            return;
        }
        repo = FirestoreRepository.getInstance();
        messages.setValue(repo.getMessagesFromRepo());
    }

    public MutableLiveData<FirestoreRecyclerOptions<Message>> getMessages() {
        return messages;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }
}
