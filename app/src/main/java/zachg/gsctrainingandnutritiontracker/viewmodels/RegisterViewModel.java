package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.widget.EditText;

import androidx.lifecycle.ViewModel;

import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class RegisterViewModel extends ViewModel {

    FirestoreRepository repo = new FirestoreRepository();

    public void init() {}

    public boolean validate(String email) {
        boolean isValid = repo.validate(email);
        return isValid;
    }
}
