package zachg.gsctrainingandnutritiontracker.viewmodels;

import androidx.lifecycle.ViewModel;

import zachg.gsctrainingandnutritiontracker.models.User;

public class ClientProfileViewModel extends ViewModel {

    private User currentUser = new User();

    public ClientProfileViewModel(User user) {
        this.currentUser = user;
        this.currentUser.setClientName(user.getClientName());
    }
}
