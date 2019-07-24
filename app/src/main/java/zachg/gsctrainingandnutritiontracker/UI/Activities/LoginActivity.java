package zachg.gsctrainingandnutritiontracker.UI.Activities;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import zachg.gsctrainingandnutritiontracker.UI.Fragments.LoginFragment;

public class LoginActivity extends FragmentActivity {

    protected Fragment createFragment() {
        return new LoginFragment();
    }

}