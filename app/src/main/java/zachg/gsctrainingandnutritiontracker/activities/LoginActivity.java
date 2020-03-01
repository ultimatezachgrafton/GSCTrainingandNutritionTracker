package zachg.gsctrainingandnutritiontracker.activities;

import androidx.fragment.app.Fragment;

import zachg.gsctrainingandnutritiontracker.fragments.LoginFragment;

public class LoginActivity extends SingleFragmentActivity {

    protected Fragment createFragment() {
        return new LoginFragment();
    }
}