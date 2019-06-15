package zachg.gsctrainingandnutritiontracker.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.SingleFragmentActivity;

public class LoginActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }

}