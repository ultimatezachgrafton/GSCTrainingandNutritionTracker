package zachg.gsctrainingandnutritiontracker.viewmodels;

import androidx.lifecycle.ViewModel;

import zachg.gsctrainingandnutritiontracker.models.User;

public class ClientProfileViewModel extends ViewModel {

    private User currentUser = new User();

    public ClientProfileViewModel() {}

    public ClientProfileViewModel(User user) {
        this.currentUser = currentUser;
        this.currentUser.setClientName(user.getClientName());
        this.currentUser.setDateJoined(user.getDateJoined());
        this.currentUser.setIsAdminString(user.getIsAdmin());
    }
}
