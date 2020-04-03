package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.telephony.PhoneNumberUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import zachg.gsctrainingandnutritiontracker.models.SingleLiveEvent;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class RegisterViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo = new FirestoreRepository();
    public FirebaseFirestore db = FirebaseFirestore.getInstance();

    public String firstName, lastName, phoneNumber, email, password, confirmPassword;

    public MutableLiveData<String> onError = new MutableLiveData<>();
    public MutableLiveData<String> onSuccess = new MutableLiveData<>();
    public SingleLiveEvent<Boolean> isUserInputValid = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> isRegistrationComplete = new SingleLiveEvent<>();

    private static String REGISTER_ERROR = "Please fill in all fields";
    private static String PASSWORD_ERROR = "Passwords do not match.";
    private static String CLIENT_ADDED = "Registering client! Please give us a moment...";
    private static String EMAIL_BADLY_FORMATTED = "Email is badly formatted.";
    private static String PASSWORD_BADLY_FORMATTED = "Password must be at least six characters long.";
    private static String PHONE_BADLY_FORMATTED = "Phone number be 10 digits long.";
    private static String DUPLICATE_EMAIL = "This email is already registered.";

    private String TAG = "RegisterViewModel";

    public void init() {}

    // Checks validity, displays error statements
    public void registerUserCheck(final String firstName, final String lastName, final String phoneNumber,
                                  final String email, final String password, final String confirmPassword) {
        if (!isRegisterReady(firstName, lastName, email, phoneNumber, password, confirmPassword)) {
            onError.setValue(REGISTER_ERROR);
            return;
        }
        if (!isEmailValid(email)) {
            onError.setValue(EMAIL_BADLY_FORMATTED);
            return;
        }
        if (!isPasswordValid(password)){
            onError.setValue(PASSWORD_BADLY_FORMATTED);
            return;
        };
        if (!doPasswordsMatch(password, confirmPassword)) {
            onError.setValue(PASSWORD_ERROR);
            return;
        }
        isEmailDuplicate(email);
    }

    // Checks if all required fields are filled in
    public boolean isRegisterReady(String firstName, String lastName, String phoneNumber, String email, String password, String confirmPassword) {
        if (firstName != null && lastName != null && phoneNumber != null &&
                email != null && password != null && confirmPassword != null) return true;
        else return false;
    }

    // Checks if passwords match
    public boolean doPasswordsMatch(String password, String confirmPassword) {
        if (password.equals(confirmPassword)) return true;
        else return false;
    }

    // Checks if email input is valid
    public boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Checks if password is long enough
    public boolean isPasswordValid(String password) {
        if (password.length() >= 6) {
            return true;
        } else {
            return false;
        }
    }

    // Checks if email is already used in database
    public void isEmailDuplicate(String email) {
        repo.setQuerySnapshotOnCompleteListener(this);
        repo.duplicateEmailCheck(email);
    }

    // Sets user values
    public void setUserValues(String firstName, String lastName, String phoneNumber, String email, String password) {
        User user = new User(firstName, lastName, phoneNumber, email, password);
        registerFirebaseUser(user);
    }

    // Registers user
    public void registerFirebaseUser(User user) {
        repo.registerFirebaseUser(user);
        onError.setValue(CLIENT_ADDED);
        isRegistrationComplete.setValue(true);
    }

    // Handles isEmailDuplicate check
    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        QuerySnapshot qs = task.getResult();
        if (qs.size() > 0) {
            onError.setValue(DUPLICATE_EMAIL);
        } else {
            Log.d(TAG, "duplicate checked");
            isUserInputValid.setValue(true);
        }
    }
}