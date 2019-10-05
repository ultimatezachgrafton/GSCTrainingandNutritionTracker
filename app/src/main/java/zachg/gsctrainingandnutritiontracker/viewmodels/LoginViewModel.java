package zachg.gsctrainingandnutritiontracker.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class LoginViewModel extends ViewModel {

    private FirebaseAuth auth;
    private FirestoreRepository repo = new FirestoreRepository();
    public User currentUser;
    public String email;
    public String password;
    private MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>(), isAdmin = new MutableLiveData<>(), isValid = new MutableLiveData<>();
    private MutableLiveData<User> user = new MutableLiveData<>();

    public LoginViewModel() {}

    public void init() {
        auth = FirebaseAuth.getInstance();   // Initialize FirebaseAuth
        if (auth.getCurrentUser() == null) { // If User is not logged in, set isLoggedIn to false
            getIsLoggedIn();
        } else {                             // Get User
            getUser();
        }
    }

    public void onClick(String email, String password) {
        // authenticate
        if (email != null && password != null) {
            auth.signInWithEmailAndPassword(email, password);
            // TODO: check if successful...
            repo.getCurrentUser(email);
        }
    }

    public MutableLiveData<Boolean> getIsLoggedIn() {
        isLoggedIn.setValue(false);
        return isLoggedIn;
    }

    private MutableLiveData<User> getUser() {
        User currentUser = new User();
        currentUser = repo.getCurrentUser(auth.getCurrentUser().getEmail());
        user.setValue(currentUser);
        setIsAdminBool(currentUser);
        return user;
    }

    public MutableLiveData<Boolean> setIsAdminBool(User currentUser) {
        if (currentUser.getIsAdmin()) {
            isAdmin.setValue(true);
            return isAdmin;
        } else {
            isAdmin.setValue(false);
            return isAdmin;
        }
    }
}
