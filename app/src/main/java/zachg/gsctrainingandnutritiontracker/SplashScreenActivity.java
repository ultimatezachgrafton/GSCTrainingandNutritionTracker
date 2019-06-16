package zachg.gsctrainingandnutritiontracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import zachg.gsctrainingandnutritiontracker.login.LoginActivity;
import zachg.gsctrainingandnutritiontracker.login.LoginHandler;

public class SplashScreenActivity extends SingleFragmentActivity {

    private FirebaseAuth mAuth;
    private LoginHandler mLoginHandler;

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.d("Zach rules", "Splashy splooosh!");
        mLoginHandler = new LoginHandler();
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
}