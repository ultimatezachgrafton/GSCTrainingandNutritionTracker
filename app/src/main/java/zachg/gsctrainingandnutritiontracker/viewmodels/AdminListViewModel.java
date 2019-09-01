package zachg.gsctrainingandnutritiontracker.viewmodels;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class AdminListViewModel extends ViewModel {

    private MutableLiveData<FirestoreRecyclerOptions<User>> mUsers = new MutableLiveData<>();
    private FirestoreRepository mRepo;
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();

    public void init() {
        mRepo = FirestoreRepository.getInstance();
        mUsers.setValue(mRepo.getUsersFromRepo());
    }

    public MutableLiveData<FirestoreRecyclerOptions<User>> getUsers() {
        return mUsers;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return mIsUpdating;
    }
}