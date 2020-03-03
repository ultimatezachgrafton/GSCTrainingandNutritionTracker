package zachg.gsctrainingandnutritiontracker.viewmodels;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class RegisterViewModel extends ViewModel {

    private FirestoreRepository repo = new FirestoreRepository();
    public FirebaseFirestore db = FirebaseFirestore.getInstance();

    public String firstName, lastName, phoneNumber, email, password;

    public ObservableField<String> etFirstName = new ObservableField<>();
    public ObservableField<String> etLastName = new ObservableField<>();
    public ObservableField<String> etEmail = new ObservableField<>();
    public ObservableField<String> etPhoneNumber = new ObservableField<>();
    public ObservableField<String> etPassword = new ObservableField<>();
    public ObservableField<String> etConfirmPassword = new ObservableField<>();
    public MutableLiveData<User> newUser = new MutableLiveData<>();
    public MutableLiveData<String> onError = new MutableLiveData<>();

    private static String REGISTER_ERROR = "Please fill in all fields";
    private static String PASSWORD_ERROR = "Passwords do not match.";
    private static String CLIENT_ADDED = "Registering client! Please give us a moment...";

    private String TAG = "RegisterViewModel";

    public void init() {}

    public void registerUserCheck(final String firstName, final String lastName, final String phoneNumber,
                                  final String email, final String password, String confirmPassword) {
        if (!isRegisterReady(firstName, lastName, email, password, confirmPassword)) {
            onError.setValue(REGISTER_ERROR);
            return;
        }
        if (!doPasswordsMatch(password, confirmPassword)) {
            onError.setValue(PASSWORD_ERROR);
            return;
        }
        setUserValues(firstName, lastName, phoneNumber, email, password);
        registerUser();
    }

    public void registerUser() {
//        int workoutDay = 1;
        User user = new User(firstName, lastName, phoneNumber, email, password);//, workoutDay);
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

    // sets user values
    public void setUserValues(String firstName, String lastName, String phoneNumber, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    // TODO: if duplicate email, display message
}
