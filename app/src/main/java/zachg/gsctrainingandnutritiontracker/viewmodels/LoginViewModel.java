package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class LoginViewModel extends ViewModel {

    private FirestoreRepository repo = new FirestoreRepository();
    public MutableLiveData<String> email = new MutableLiveData<>(), password = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();
    public MutableLiveData<User> userMutable = new MutableLiveData<>();

    public LoginViewModel() {}

    public void init() {
        FirebaseUser fUser = repo.getUser();
        if (fUser == null) {                                        // Check to see if a user is logged in
            setIsLoggedIn(false);                                   // If a user is not logged in, set isLoggedIn to false
        } else {
            setIsLoggedIn(true);
            userMutable.setValue(repo.getUserByEmail(fUser.getEmail()));   // If a user is logged in, get that User's information
        }
    }

    public MutableLiveData<Boolean> setIsLoggedIn(Boolean bool) {
        isLoggedIn.setValue(bool);
        return isLoggedIn;
    }

    public MutableLiveData<User> getUser() {
        if (userMutable == null) {
            userMutable = new MutableLiveData<>();
        }
        return userMutable;
    }

    public void logIn() {
        email.setValue(email.getValue());
        password.setValue("b");
        User user = new User(email.getValue(), password.getValue());
        userMutable.setValue(user);
        Log.d("plum", ": " + email.getValue() + password.getValue());
        if (email.getValue() != null && password.getValue() != null) {
            userMutable.setValue(repo.getUserByEmailPassword(String.valueOf(email), String.valueOf(password)));
            // TODO: if user == null...
            // Toast: this person doesn't exist, please register
            Log.d("plum", "isvalid");
        } else {
            // Toast: Please enter user information
            Log.d("plum", "not valid");
        }
    }
}
