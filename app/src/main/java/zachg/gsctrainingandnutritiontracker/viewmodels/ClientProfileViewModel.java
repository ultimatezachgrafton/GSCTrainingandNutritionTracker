package zachg.gsctrainingandnutritiontracker.viewmodels;

import androidx.lifecycle.ViewModel;

import zachg.gsctrainingandnutritiontracker.models.User;

public class ClientProfileViewModel extends ViewModel {

    private User mCurrentUser = new User();

    public ClientProfileViewModel() {
        // required empty constructor
    }

    public ClientProfileViewModel(User currentUser) {
        this.mCurrentUser = currentUser;
        this.mCurrentUser.setClientName(currentUser.getClientName());
        this.mCurrentUser.setGender(currentUser.getGender());
        this.mCurrentUser.setDateJoined(currentUser.getDateJoined());
        this.mCurrentUser.setBirthdate(currentUser.getBirthdate());
        this.mCurrentUser.setIsAdminString(currentUser.getIsAdmin());
    }
}
