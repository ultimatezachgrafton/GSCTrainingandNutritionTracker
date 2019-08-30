package zachg.gsctrainingandnutritiontracker.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import zachg.gsctrainingandnutritiontracker.models.Message;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class InboxViewModel extends ViewModel {

    private MutableLiveData<FirestoreRecyclerOptions<Message>> mMessages = new MutableLiveData<>();
    private FirestoreRepository mRepo;
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();

    public void init() {
        if (mMessages.getValue() != null) {
            return;
        }
        mRepo = FirestoreRepository.getInstance();
        mMessages.setValue(mRepo.getMessagesFromRepo());
    }

    public MutableLiveData<FirestoreRecyclerOptions<Message>> getMessages() {
        return mMessages;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return mIsUpdating;
    }
}
