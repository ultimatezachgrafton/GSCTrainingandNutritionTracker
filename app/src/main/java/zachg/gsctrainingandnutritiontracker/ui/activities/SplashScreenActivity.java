package zachg.gsctrainingandnutritiontracker.ui.activities;

import androidx.fragment.app.Fragment;

import zachg.gsctrainingandnutritiontracker.ui.fragments.SplashScreenFragment;

// Provides a splash graphic while the app checks if the user is logged in.
// If the user is logged in, they go directly to their profile. Otherwise, LoginFragment is loaded.
public class SplashScreenActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new SplashScreenFragment();
    }
}