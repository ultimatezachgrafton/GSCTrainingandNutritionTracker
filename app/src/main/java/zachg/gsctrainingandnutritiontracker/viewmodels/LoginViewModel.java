package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import zachg.gsctrainingandnutritiontracker.models.SingleLiveEvent;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class LoginViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo = new FirestoreRepository();
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private boolean isDuplicate = false;
    public ObservableField<String> email = new ObservableField<>();
    public ObservableField<String> password = new ObservableField<>();
    private SingleLiveEvent<Boolean> isLoggingIn = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> isLogInNull = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> doesUserExist = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> isLoggedIn = new SingleLiveEvent<>();
    private SingleLiveEvent<User> userSingleLiveEvent = new SingleLiveEvent<>();

    public String TAG = "LoginViewModel";

    // Checks if user is logged in
    public void init() {}

    // Verifies user exists by the email and password provided
    public void verifyUser(String email, String password) {
        isLogInNull.setValue(false);
        if (!isLoginNull(email, password)) {
            isLogInNull.setValue(true);
            return;
        } else {
            isLoggingIn.setValue(true);
            this.isDuplicate = true;
            repo.setQuerySnapshotOnCompleteListener(this);
            repo.queryUserByEmailPassword(email, password);
        }
    }

    public void checkFirebaseUser() {
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null)
        firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            loginWithEmail(firebaseUser.getEmail());
        }
    }

    // Verifies login fields are not null
    public boolean isLoginNull(String email, String password) {
        Log.d(TAG, "isLoginNull");
        return email != null && password != null;
    }

    public void loginWithEmail(String email) {
        repo.setQuerySnapshotOnCompleteListener(this);
        repo.loginWithEmail(email);
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        QuerySnapshot qs = task.getResult();
        if (qs.size() == 0) {
            doesUserExist.setValue(false);
            Log.d(TAG, "Error getting documents: ", task.getException());
            return;
        } else {
            for (QueryDocumentSnapshot doc : qs) {
                User user = doc.toObject(User.class);
                if (isDuplicate) {
                    doesUserExist.setValue(true);
                }
                assignUserValue(user);
                repo.signIn(user.getEmail(), user.getPassword());
            }
        }
    }

    public void assignUserValue(User user) {
        userSingleLiveEvent.setValue(user);
    }
    public SingleLiveEvent<User> getUserSingleLiveEvent() {
        return userSingleLiveEvent;
    }
    public void onUserSingleLiveEvent(User user) {
        userSingleLiveEvent.setValue(user);
    }
    public SingleLiveEvent<Boolean> getIsLoggingIn() {
        return isLoggingIn;
    }
    public SingleLiveEvent<Boolean> getIsLoggedIn() {
        return isLoggedIn;
    }
    public SingleLiveEvent<Boolean> getDoesUserExist() {
        return doesUserExist;
    }
    public SingleLiveEvent<Boolean> getIsLogInNull() {
        return isLogInNull;
    }

}