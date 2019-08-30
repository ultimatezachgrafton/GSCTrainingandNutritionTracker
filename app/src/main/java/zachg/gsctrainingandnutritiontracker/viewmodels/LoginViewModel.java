package zachg.gsctrainingandnutritiontracker.viewmodels;

import com.google.firebase.auth.FirebaseAuth;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;
import zachg.gsctrainingandnutritiontracker.utils.LoginListener;

public class LoginViewModel {

    private FirebaseAuth mAuth;
    private FirestoreRepository mRepo = new FirestoreRepository();
    private LoginListener mLoginListener;
    public User mCurrentUser;
    public String email;
    public String password;
    public boolean isAdmin;

    public LoginViewModel() {}

    public LoginViewModel(LoginListener loginListener) {
        mLoginListener = loginListener;
    }

    public void init() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            // create user to
            // go to apropos frag
            mRepo.getCurrentUser(mAuth.getCurrentUser().getEmail());
        }
    }

    public void onClick(String email, String password) {
        // authenticate
        if (email != null && password != null) {
            mAuth.signInWithEmailAndPassword(email, password);
            // TODO: check if successful...
            mRepo.getCurrentUser(email);
        }
    }
}
