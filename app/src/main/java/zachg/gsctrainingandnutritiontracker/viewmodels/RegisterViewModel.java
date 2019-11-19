package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RegisterViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo = new FirestoreRepository();
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userColRef = db.collection("users");

    public String firstName, lastName, email, password;

    public ObservableField<String> etFirstName = new ObservableField<>();
    public ObservableField<String> etLastName = new ObservableField<>();
    public ObservableField<String> etEmail = new ObservableField<>();
    public ObservableField<String> etPassword = new ObservableField<>();
    public ObservableField<String> etConfirmPassword = new ObservableField<>();
    public MutableLiveData<User> newUser = new MutableLiveData<>();
    public MutableLiveData<String> onError = new MutableLiveData<>();
    public MutableLiveData<Boolean> isDuplicate;

    private static String REGISTER_ERROR = "Please fill in all fields";
    private static String PASSWORD_ERROR = "Passwords do not match.";
    private static String DUPLICATE_ERROR = "This email is already in use.";
    private static String CLIENT_ADDED = "Registering client! Please give us a moment...";

    public void init() {
        repo.setSnapshotOnCompleteListener(this);
    }

    public void registerUserCheck(final String firstName, final String lastName, final String email, final String password, String confirmPassword) {
        if (!isRegisterReady(firstName, lastName, email, password, confirmPassword)) {
            onError.setValue(REGISTER_ERROR);
            return;
        }
        if (!doPasswordsMatch(password, confirmPassword)) {
            onError.setValue(PASSWORD_ERROR);
            return;
        }
        setUserValues(firstName, lastName, email, password);
        duplicateUserCheck(email);
    }

    public void registerUser() {
        User user = new User(firstName, lastName, email, password);
        repo.registerUser(user);
        newUser.setValue(user);
        onError.setValue(CLIENT_ADDED);
    }

    // Checks if all required fields are filled in
    public boolean isRegisterReady(String firstName, String lastName, String email, String password, String confirmPassword) {
        if (firstName != null && lastName != null && email != null && password != null && confirmPassword != null) return true;
        else return false;
    }

    // Checks if passwords match
    public boolean doPasswordsMatch(String password, String confirmPassword) {
        if (password.equals(confirmPassword)) return true;
        else return false;
    }

    // Checks if User's email is already used
    public void duplicateUserCheck(String email) {
        repo.duplicateEmailCheck(email);
    }

    public void setUserValues(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public void setDuplicateTrue() {
        onError.setValue(DUPLICATE_ERROR);
        return;
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        Log.d(TAG, "oncomplete");
        if (task.isSuccessful()) {
            Log.d(TAG, "task size:" + String.valueOf(task.getResult().size()));
            if (task.getResult().size() > 0) {
                isDuplicate.setValue(true);
            } else {
                isDuplicate.setValue(false);
            }
        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
        }
    }
}
