package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class AdminUserListViewModel extends ViewModel {

    private FirestoreRepository repo;
    private User currentUser = new User();
    private MutableLiveData<FirestoreRecyclerOptions<User>> users = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    public String TAG = "AdminUserListViewModel";

    public void init(User user) {
        this.currentUser = user;
        repo = FirestoreRepository.getInstance();
        users.setValue(repo.getUsersFromRepo());
    }

    public MutableLiveData<FirestoreRecyclerOptions<User>> getUsers() {
        return users;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }

    public User onItemClicked(DocumentSnapshot documentSnapshot, int position) {
        // Fetches currentUser
        User currentUser = documentSnapshot.toObject(User.class);
        String id = documentSnapshot.getId();
        String path = documentSnapshot.getReference().getPath();
        return currentUser;
    }
}