package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class ClientProfileViewModel extends ViewModel {

    private User mCurrentUser = new User();

    public ClientProfileViewModel() {
        // required empty constructor
    }

    public ClientProfileViewModel(User currentUser) {
        this.mCurrentUser = currentUser;
        this.mCurrentUser.setClientName(currentUser.getFirstName(), currentUser.getLastName());
        this.mCurrentUser.setGender(currentUser.getGender());
        this.mCurrentUser.setDateJoined(currentUser.getDateJoined());
        this.mCurrentUser.setBirthdate(currentUser.getBirthdate());
        this.mCurrentUser.setIsAdminString(currentUser.getIsAdmin());
    }
}
