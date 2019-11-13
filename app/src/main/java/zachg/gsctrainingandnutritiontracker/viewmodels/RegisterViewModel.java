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
    public User user = new User();

    private String registerError = "Please fill in all fields";
    private String passwordError = "Passwords do not match.";
    private String duplicateError = "This email is already in use.";

    public void init() {
        repo.setSnapshotOnCompleteListener(this);
    }

    public void registerUser(String firstName, String lastName, String email, String password, String confirmPassword) {
        if (!isRegisterReady(firstName, lastName, email, password, confirmPassword)) {
            onError.setValue(registerError);
            return;
        }
        if (!doPasswordsMatch(password, confirmPassword)) {
            onError.setValue(passwordError);
            return;
        }
        if (!duplicateUserCheck(email)) {
            onError.setValue(duplicateError);
            return;
        }

        // TODO: set user values
        newUser.setValue(user);

        repo.registerUser(user);
    }

    // Checks if all required fields are filled in
    public boolean isRegisterReady(String firstName, String lastName, String email, String password, String confirmPassword) {
        if (firstName != null && lastName != null && email != null && password != null && confirmPassword != null) return true;
        else return false;
    }

    // Checks if passwords match
    public boolean doPasswordsMatch(String password, String confirmPassword) {
        if (password == confirmPassword) return true;
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
                user = doc.toObject(User.class);
                newUser.setValue(user);
            }
        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
        }
    }
}
