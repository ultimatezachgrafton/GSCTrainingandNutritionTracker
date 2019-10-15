package zachg.gsctrainingandnutritiontracker.viewmodels;

import androidx.lifecycle.ViewModel;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class RegisterViewModel extends ViewModel {

    FirestoreRepository repo = new FirestoreRepository();

    public void registerUser(User user) {
        repo.registerUser(user);
    }

    public boolean confirmPassword(String password, String confirm) {
        if (password == confirm) {
            return true;
        } else {
            return false;
        }
    }

    // Checks if User's email is already used
    public boolean duplicateUserCheck(String email) {
        if (repo.duplicateEmailCheck(email)) {
            return true;
        } else {
            return false;
        }
    }
}
