package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentSplashBinding;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.SplashScreenViewModel;

import static android.content.ContentValues.TAG;

public class SplashScreenFragment extends Fragment {

    private FragmentSplashBinding binding;
    private User user = new User();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSplashBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();

        // Gets ViewModel instance to observe its LiveData
        SplashScreenViewModel splashScreenViewModel = ViewModelProviders.of(getActivity()).get(SplashScreenViewModel.class);
        splashScreenViewModel.init();

        final Observer<Boolean> logInObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean newBoolean) {
                if (newBoolean == true) {
                    goToLogin();
                }
            }
        };

        final Observer<User> userObserver = new Observer<User>() {
            @Override
            public void onChanged(@Nullable final User newUser) {
                user = newUser;
            }
        };

        final Observer<Boolean> adminObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (true) {
                    goToAdminList();
                } else {
                    goToProfile(user);
                }
            }
        };
        return v;
    }

    public void goToLogin() {
        Log.d(TAG, "login");
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new LoginFragment()).addToBackStack(null).commit();
    }

    public void goToProfile(User user) {
        Log.d(TAG, "profile");
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ClientProfileFragment(user)).addToBackStack(null).commit();
    }

    public void goToAdminList() {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new AdminUserListFragment()).addToBackStack(null).commit();
    }
}
