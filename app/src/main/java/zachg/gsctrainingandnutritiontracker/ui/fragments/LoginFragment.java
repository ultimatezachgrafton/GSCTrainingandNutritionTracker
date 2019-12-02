package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentLoginBinding;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.LoginViewModel;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private User user = new User();
    LoginViewModel loginViewModel = new LoginViewModel();

    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate and bind the layout for this fragment_report_list
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        // Gets ViewModel instance to observe its LiveData
        binding.setModel(loginViewModel);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.init();

        // Bind this fragment_report_list
        binding.setFragment(this);

        // Bind User
        binding.setUser(user);

        final Observer<Boolean> isLoggedInObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                if (true) {
                    if (user.getIsAdmin()) {
                        goToAdminList(user);
                    } else if (!user.getIsAdmin()) {
                        goToProfile(user);
                    }
                }
            }
        };

        loginViewModel.currentUser.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user == null) {
                    Toast.makeText(getContext(), "That user does not exist.", Toast.LENGTH_SHORT).show();
                } else if (user.getIsAdmin()) {
                    goToAdminList(user);
                } else if (!user.getIsAdmin()) {
                    goToProfile(user);
                }
            }
        });

        return v;
    }

    public void onLoginClick(final String email, final String password) {
        // TODO: make toast into accurate progress bar
        Toast.makeText(getContext(), "Logging in...", Toast.LENGTH_LONG).show();
        // Check if login values are valid
        loginViewModel.verifyUser(email, password);
    }

    public void onRegisterClick() {
        loginViewModel.currentUser.removeObservers(this);
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new RegisterFragment()).addToBackStack(null).commit();
    }

    public void goToProfile(User user) {
        Toast.makeText(getContext(), "Logging in...", Toast.LENGTH_LONG).show();
        loginViewModel.currentUser.removeObservers(this);
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ClientProfileFragment(user)).addToBackStack(null).commit();
    }

    public void goToAdminList(User user) {
        Toast.makeText(getContext(), "Logging in...", Toast.LENGTH_LONG).show();
        loginViewModel.currentUser.removeObservers(this);
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new AdminUserListFragment(user)).addToBackStack(null).commit();
    }
}