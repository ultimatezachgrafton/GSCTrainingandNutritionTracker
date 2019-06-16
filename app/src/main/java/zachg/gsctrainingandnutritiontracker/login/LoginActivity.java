package zachg.gsctrainingandnutritiontracker.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class LoginActivity extends FragmentActivity {

    protected Fragment createFragment() {
        return new LoginFragment();
    }

}