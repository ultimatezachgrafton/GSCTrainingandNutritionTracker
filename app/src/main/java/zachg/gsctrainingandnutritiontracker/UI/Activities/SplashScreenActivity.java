package zachg.gsctrainingandnutritiontracker.UI.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.ViewModels.LoginHandler;
import zachg.gsctrainingandnutritiontracker.ViewModels.LoginListener;

public class SplashScreenActivity extends FragmentActivity implements LoginListener {

    private FirebaseAuth mAuth;
    private LoginHandler mLoginHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mLoginHandler = new LoginHandler(this);
        login();
    }

    private void login() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            // create user to
            // go to apropos frag
            mLoginHandler.onLogin(mAuth.getCurrentUser().getEmail());
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
}