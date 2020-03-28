package zachg.gsctrainingandnutritiontracker.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.fragments.LoginFragment;

public class LoginActivity extends SingleFragmentActivity {

    protected Fragment createFragment() { return new LoginFragment();  }
}