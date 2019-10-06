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
import zachg.gsctrainingandnutritiontracker.databinding.FragmentLoginBinding;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.LoginViewModel;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private String email, password;
    private User user = new User();
    LoginViewModel loginViewModel = new LoginViewModel();

    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        // Gets ViewModel instance to observe its LiveData
        loginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
        loginViewModel.init();
        binding.setModel(loginViewModel);

        final Observer<Boolean> logInObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean newBoolean) {
                if (newBoolean == true) {
                    goToProfile(user);
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

    // auth
    public void authenticate() {
        if (email != null && password != null) {
            loginViewModel.onClick(email, password);
        }
    }

    public void goToProfile(User user) {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ClientProfileFragment(user)).addToBackStack(null).commit();
    }

    public void goToAdminList() {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new AdminUserListFragment()).addToBackStack(null).commit();
    }

    public void goToRegister() {
        Log.d("plum", "plum");
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new RegisterFragment()).addToBackStack(null).commit();
    }
}