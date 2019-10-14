package zachg.gsctrainingandnutritiontracker.viewmodels;

import androidx.lifecycle.ViewModel;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class RegisterViewModel extends ViewModel {

    FirestoreRepository repo = new FirestoreRepository();

    public void registerUser(User user) {
        repo.registerUser(user);
    }

    public void validate() {

    }

    public boolean confirmPassword(String password, String confirm) {
        if (password == confirm) {
            return true;
        } else {
            return false;
        }
    }

    public boolean duplicateUserCheck(String email) {
        if (repo.validate(email)) {
            return true;
        } else {
            return false;
        }
    }
}
