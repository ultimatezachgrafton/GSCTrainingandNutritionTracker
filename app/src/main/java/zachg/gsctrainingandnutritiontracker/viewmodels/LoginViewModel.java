package zachg.gsctrainingandnutritiontracker.viewmodels;

import com.google.firebase.auth.FirebaseAuth;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;
import zachg.gsctrainingandnutritiontracker.utils.LoginListener;

public class LoginViewModel {

    private FirebaseAuth auth;
    private FirestoreRepository repo = new FirestoreRepository();
    private LoginListener loginListener;
    public User currentUser;
    public String email;
    public String password;
    public boolean isAdmin;

    public LoginViewModel() {}

    public LoginViewModel(LoginListener loginListener) {
        loginListener = loginListener;
    }

    public void init() {
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // create user to
            // go to apropos frag
            repo.getCurrentUser(auth.getCurrentUser().getEmail());
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
}
