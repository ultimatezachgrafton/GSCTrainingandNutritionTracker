package zachg.gsctrainingandnutritiontracker.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.fragments.LoginFragment;

public class LoginActivity extends SingleFragmentActivity {

    protected Fragment createFragment() {
        return new LoginFragment();
    }
}