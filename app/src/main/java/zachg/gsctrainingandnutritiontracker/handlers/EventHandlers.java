package zachg.gsctrainingandnutritiontracker.handlers;

import android.view.View;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.ui.fragments.LoginFragment;
import zachg.gsctrainingandnutritiontracker.viewmodels.LoginViewModel;

public class EventHandlers {

    public void goToProfile(View view) {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.goToProfile();
    }

    public void goToAdminList(View view) {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.goToAdminList();
    }
    public void goToRegister(View view) {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.goToRegister();
    }

    public void logIn(View view) {
        LoginViewModel loginViewModel = new LoginViewModel();
        loginViewModel.logIn();
    }
}
