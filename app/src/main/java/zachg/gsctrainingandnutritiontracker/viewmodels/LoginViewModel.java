package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class LoginViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo = new FirestoreRepository();
    public ObservableField<String> email, password = new ObservableField<>();
    public MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();
    public MutableLiveData<FirebaseUser> firebaseUser = new MutableLiveData<>();
    public MutableLiveData<User> currentUser = new MutableLiveData<>();
    public User user = new User();

    public String TAG = "LoginViewModel";

    // Checks if user is logged in
    public void init() {
        FirebaseUser fUser = repo.getFireBaseUser();
        if (fUser == null) {                                // Check to see if a user is logged in
            setIsLoggedIn(false);
            Log.d(TAG, "loggedinfalse");               // If a user is not logged in, set isLoggedIn to false
        } else {
            setIsLoggedIn(true);
            user = repo.getUserByEmail(fUser.getEmail());
            Log.d(TAG, "loggedintrue");                // If a user is logged in, get that User's information
        }
        repo.setSnapshotOnCompleteListener(this);
    }

    // Sets logged in state for observer
    public MutableLiveData<Boolean> setIsLoggedIn(Boolean bool) {
        isLoggedIn.setValue(bool);
        return isLoggedIn;
    }

    // Verifies user exists by the email and password provided
    public void verifyUser(String email, String password) {
        if (!isLoginReady(email, password)) return;
        repo.queryUserByEmailPassword(email, password);
    }

    // Verifies login fields are not null
    public boolean isLoginReady(String email, String password) {
        return email != null && password != null;
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot doc : task.getResult()) {
                user = doc.toObject(User.class);
                currentUser.setValue(user);
            }
        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
            return;
        }
    }
}
