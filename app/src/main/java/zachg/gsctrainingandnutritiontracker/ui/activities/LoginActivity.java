package zachg.gsctrainingandnutritiontracker.ui.activities;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import zachg.gsctrainingandnutritiontracker.ui.fragments.LoginFragment;

public class LoginActivity extends FragmentActivity {

    protected Fragment createFragment() {
        return new LoginFragment();
    }

}