package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class LoginViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo = new FirestoreRepository();
    public ObservableField<String> email = new ObservableField<>();
    public ObservableField<String> password = new ObservableField<>();
    public MutableLiveData<Boolean> isLoggingIn = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLogInNull = new MutableLiveData<>();
    public MutableLiveData<Boolean> doesUserExist = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();
    public MutableLiveData<FirebaseUser> firebaseUser = new MutableLiveData<>();
    public MutableLiveData<User> currentUser = new MutableLiveData<>();
    public User user = new User();

    public String TAG = "LoginViewModel";

    // Checks if user is logged in
    public void init(FirebaseUser fUser) {
        Log.d(TAG, "init");
        if (fUser == null) {
            isLoggedIn.setValue(false);
        } else {
            isLoggedIn.setValue(true);
            repo.setQuerySnapshotOnCompleteListener(this);
            repo.getUserByEmail(fUser.getEmail());
        }
    }

    // Verifies user exists by the email and password provided
    public void verifyUser(String email, String password) {
        isLogInNull.setValue(false);
        if (!isLoginNull(email, password)) {
            isLogInNull.setValue(true);
            return;
        } else {
            isLoggingIn.setValue(true);
            repo.queryUserByEmailPassword(email, password);
        }
    }

    // Verifies login fields are not null
    public boolean isLoginNull(String email, String password) {
        return email != null && password != null;
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
                user = doc.toObject(User.class);
                currentUser.setValue(user);
                doesUserExist.setValue(true);
                repo.signIn(user.getEmail(), user.getPassword());
            }
        }
    }

    public void clearLiveData() {
        currentUser.setValue(null);
        isLoggingIn.setValue(false);
    }
}