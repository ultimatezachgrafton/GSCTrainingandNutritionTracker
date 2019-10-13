package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;
import android.widget.EditText;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class LoginViewModel extends ViewModel {

    private FirestoreRepository repo = new FirestoreRepository();
    public final ObservableField<String> email = new ObservableField<>(), password = new ObservableField<>();
    public MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();
    public User user = new User();

    public LoginViewModel() {}

    public void init() {
        FirebaseUser fUser = repo.getUser();
        if (fUser == null) {                                        // Check to see if a user is logged in
            setIsLoggedIn(false);                                   // If a user is not logged in, set isLoggedIn to false
        } else {
            setIsLoggedIn(true);
            user = repo.getUserByEmail(fUser.getEmail());   // If a user is logged in, get that User's information
        }
    }

    public MutableLiveData<Boolean> setIsLoggedIn(Boolean bool) {
        isLoggedIn.setValue(bool);
        return isLoggedIn;
    }

    public void logIn() {
        Log.d("plum", ": " + user.getPassword() + user.getEmail());
        if (email.get() != null && password.get() != null) {
            user = repo.getUserByEmailPassword(String.valueOf(email), String.valueOf(password));
            // TODO: if user == null...
            // Toast: this person doesn't exist, please register
            Log.d("plum", "isvalid");
        } else {
            // Toast: Please enter user information
            Log.d("plum", "not valid");
        }
    }

    public void getEmail() {

    }

    public void setEmail() {

    }

    public void setPassword() {

    }

    public void getPassword() {

    }
}
