package zachg.gsctrainingandnutritiontracker.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import zachg.gsctrainingandnutritiontracker.SingleFragmentActivity;

public class RegisterActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new RegisterFragment();
    }
}
