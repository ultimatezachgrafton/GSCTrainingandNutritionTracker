package zachg.gsctrainingandnutritiontracker.UI.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.ViewModels.LoginViewModel;

public class SplashScreenActivity extends FragmentActivity implements LoginViewModel.LoginListener {

    private FirebaseAuth mAuth;
    private LoginViewModel mLoginViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mLoginViewModel = new LoginViewModel(this);
        login();
    }

    private void login() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            // create user to
            // go to apropos frag
            mLoginViewModel.onLogin(mAuth.getCurrentUser().getEmail());
        } else {
            // start welcome activity for login
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            finish();
        }
    }

    // why does this go to DatePicker?
    @Override
    public void goToDatePicker() {
        startActivity(new Intent(this, AdminListActivity.class));
    }

    @Override
    public void goToAdminList() {
        startActivity(new Intent(this, AdminListActivity.class));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}