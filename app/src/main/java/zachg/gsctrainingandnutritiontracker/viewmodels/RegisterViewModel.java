package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RegisterViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo = new FirestoreRepository();

    public ObservableField<String> etFirstName, etLastName, etEmail, etPassword, etConfirmPassword = new ObservableField<>();
    public MutableLiveData<User> newUser = new MutableLiveData<>();
    public MutableLiveData<String> onError = new MutableLiveData<>();

    private static String REGISTER_ERROR = "Please fill in all fields";
    private static String PASSWORD_ERROR = "Passwords do not match.";
    private static String DUPLICATE_ERROR = "This email is already in use.";

    public void init() {
        repo.setSnapshotOnCompleteListener(this);
    }

    public void registerUserCheck(String firstName, String lastName, String email, String password, String confirmPassword) {
        if (!isRegisterReady(firstName, lastName, email, password, confirmPassword)) {
            onError.setValue(REGISTER_ERROR);
            return;
        }
        if (!doPasswordsMatch(password, confirmPassword)) {
            onError.setValue(PASSWORD_ERROR);
            return;
        }
        if (!duplicateUserCheck(email)) {
            onError.setValue(DUPLICATE_ERROR);
            return;
        }

        Log.d(TAG, "ruc2");
        User user = new User(String.valueOf(etFirstName), String.valueOf(etLastName), String.valueOf(etEmail), String.valueOf(etPassword));
        registerUser(user);
    }

    public void registerUser(User user) {
        repo.registerUser(user);
    }

    // Checks if all required fields are filled in
    public boolean isRegisterReady(String firstName, String lastName, String email, String password, String confirmPassword) {
        Log.d(TAG, "irr");
        if (firstName != null && lastName != null && email != null && password != null && confirmPassword != null) return true;
        else return false;
    }

    // Checks if passwords match
    public boolean doPasswordsMatch(String password, String confirmPassword) {
        if (password.equals(confirmPassword)) return true;
        else return false;
    }

    // Checks if User's email is already used
    public boolean duplicateUserCheck(String email) {
        return repo.duplicateEmailCheck(email);
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot doc : task.getResult()) {
                User user = doc.toObject(User.class);
                newUser.setValue(user);
            }
        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
        }
    }
}
