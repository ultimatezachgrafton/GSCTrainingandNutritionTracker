package zachg.gsctrainingandnutritiontracker;

import androidx.fragment.app.Fragment;

import zachg.gsctrainingandnutritiontracker.login.LoginFragment;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }
}
