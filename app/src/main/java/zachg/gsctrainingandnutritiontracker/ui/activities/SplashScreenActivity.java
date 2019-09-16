package zachg.gsctrainingandnutritiontracker.ui.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.viewmodels.SplashScreenViewModel;
import zachg.gsctrainingandnutritiontracker.utils.LoginListener;

public class SplashScreenActivity extends FragmentActivity implements LoginListener {

    private SplashScreenViewModel splashScreenViewModel = new SplashScreenViewModel();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashScreenViewModel = new SplashScreenViewModel();
        splashScreenViewModel.init();
    }

    @Override
    public void goToProfile() { }

    @Override
    public void goToAdminList() { }

    @Override
    public void goToLogin() { }
}
