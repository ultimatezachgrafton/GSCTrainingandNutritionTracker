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

import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.SingleLiveEvent;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class AdminUserListViewModel extends ViewModel {

    private FirestoreRepository repo = new FirestoreRepository();
    private User currentUser = new User();
    private MutableLiveData<FirestoreRecyclerOptions<User>> users = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    private SingleLiveEvent<User> userClicked = new SingleLiveEvent<>();
    public String TAG = "AdminUserListViewModel";

    public void init(User user) {
        this.currentUser = user;
        users.setValue(repo.getUsersFromRepo());
    }

    public MutableLiveData<FirestoreRecyclerOptions<User>> getUsers() {
        return users;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }

    public SingleLiveEvent<User> getUserClicked() { return userClicked; }

    public void onItemClicked(DocumentSnapshot documentSnapshot, int position) {
        // Fetches currentUser
        User currentUser = documentSnapshot.toObject(User.class);
        String id = documentSnapshot.getId();
        String path = documentSnapshot.getReference().getPath();
        userClicked.setValue(currentUser);
    }
}