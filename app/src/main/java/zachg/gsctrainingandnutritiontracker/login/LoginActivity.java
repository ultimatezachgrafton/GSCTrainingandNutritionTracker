package zachg.gsctrainingandnutritiontracker.login;

import androidx.fragment.app.Fragment;

import zachg.gsctrainingandnutritiontracker.SingleFragmentActivity;

public class LoginActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }
}