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
    public final ObservableField<String> email = new ObservableField<>(), password = new ObservableField<>();
    public MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();
    public MutableLiveData<User> user = new MutableLiveData<>();
    public String emailStr, passwordStr;

    public LoginViewModel() {}

    public void init() {
        FirebaseUser fUser = repo.getUser();
        if (fUser == null) {                                        // Check to see if a user is logged in
            setIsLoggedIn(false);                                   // If a user is not logged in, set isLoggedIn to false
        } else {
            setIsLoggedIn(true);
            user.setValue(repo.getUserByEmail(fUser.getEmail()));   // If a user is logged in, get that User's information
        }
    }

    public MutableLiveData<Boolean> setIsLoggedIn(Boolean bool) {
        isLoggedIn.setValue(bool);
        return isLoggedIn;
    }

    public MutableLiveData<User> getUser() {
        if (user == null) {
            user = new MutableLiveData<>();
        }
        return user;
    }

    public void logIn() {
        this.email.set(String.valueOf(getEmail()));
        this.password.set("b");
        User newUser = new User(email.get(), password.get());
        user.setValue(newUser);
        Log.d("plum", ": " + email.get() + password.get());
        if (email.get() != null && password.get() != null) {
            user.setValue(repo.getUserByEmailPassword(String.valueOf(email), String.valueOf(password)));
            // TODO: if user == null...
            // Toast: this person doesn't exist, please register
            Log.d("plum", "isvalid");
        } else {
            // Toast: Please enter user information
            Log.d("plum", "not valid");
        }
    }

    public String getEmail() {
        return emailStr;
    }

    public String getPassword() {
        return passwordStr;
    }

    public void setPassword(CharSequence p) {
        this.passwordStr = p.toString();
    }

    public void setEmail(CharSequence e) {
        this.emailStr = e.toString();
    }
}
