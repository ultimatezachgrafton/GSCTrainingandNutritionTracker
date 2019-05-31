package zachg.gsctrainingandnutritiontracker.login;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

import zachg.gsctrainingandnutritiontracker.SingleFragmentActivity;

public class RegisterActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new RegisterFragment();
    }

}
