package zachg.gsctrainingandnutritiontracker.viewmodels;

import com.google.firebase.auth.FirebaseAuth;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class LoginViewModel {

    private FirebaseAuth auth;
    private FirestoreRepository repo = new FirestoreRepository();
    public User currentUser;
    public String email;
    public String password;
    public boolean isAdmin;

    public LoginViewModel() {}

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
