package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AdminUserListViewModel extends ViewModel {

    private FirestoreRepository repo;
    private MutableLiveData<FirestoreRecyclerOptions<User>> users = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    public String TAG = "AdminUserListViewModel";

    public void init() {
        repo = FirestoreRepository.getInstance();
        users.setValue(repo.getUsersFromRepo());
    }

    public MutableLiveData<FirestoreRecyclerOptions<User>> getUsers() {
        return users;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }

    public void onItemClicked() {
        Log.d(TAG, "clicked in vm");
    }
}