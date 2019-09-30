package zachg.gsctrainingandnutritiontracker.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class SplashScreenViewModel extends ViewModel {

    private FirebaseAuth auth;
    private FirestoreRepository repo = new FirestoreRepository();
    private MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>(), isAdmin = new MutableLiveData<>();
    private MutableLiveData<User> user = new MutableLiveData<>();

    public void init() {
        auth = FirebaseAuth.getInstance();   // Initialize FirebaseAuth
        if (auth.getCurrentUser() == null) { // If User is not logged in, set isLoggedIn to false
            getIsLoggedIn();
        } else {                             // Get User
            getUser();
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
