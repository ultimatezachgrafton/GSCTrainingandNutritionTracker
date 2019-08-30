package zachg.gsctrainingandnutritiontracker.viewmodels;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;
import zachg.gsctrainingandnutritiontracker.ui.activities.SplashScreenActivity;

public class SplashScreenViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    private FirestoreRepository mRepo = new FirestoreRepository();
    private SplashScreenActivity mSplashScreenActivity = new SplashScreenActivity();

    public void init(){
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // If User is already logged in, goes straight to their profile
        // Otherwise, goes straight to Login screen
        if (mAuth.getCurrentUser() == null) {
            mSplashScreenActivity.goToLogin();
        } else {
            mRepo.getCurrentUser(mAuth.getCurrentUser().getEmail());
        }
    }

}
