package zachg.gsctrainingandnutritiontracker.ui.activities;

import android.util.Log;

import androidx.fragment.app.Fragment;

import zachg.gsctrainingandnutritiontracker.ui.fragments.LoginFragment;

public class LoginActivity extends SingleFragmentActivity {

    protected Fragment createFragment() {
        return new LoginFragment();
    }

}