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
    public User user = new User();

    public LoginViewModel() {}

    public void init() {
        FirebaseUser fUser = repo.getUser();
        if (fUser == null) {                                        // Check to see if a user is logged in
            setIsLoggedIn(false);                                   // If a user is not logged in, set isLoggedIn to false
        } else {
            setIsLoggedIn(true);
            user = repo.getUserByEmail(fUser.getEmail());           // If a user is logged in, get that User's information
        }
    }

    public MutableLiveData<Boolean> setIsLoggedIn(Boolean bool) {
        isLoggedIn.setValue(bool);
        return isLoggedIn;
    }

    public User verifyUser(String email, String password) {
        user = repo.getUserByEmailPassword(email, password);
        if (user.getClientName() == null) {
            return null;
        } else {
            return user;
        }
    }

    public Boolean verifyLogin(String email, String password) {
        if (email != null && password != null) {
            return true;
        } else {
            return false;
        }
    }
}
