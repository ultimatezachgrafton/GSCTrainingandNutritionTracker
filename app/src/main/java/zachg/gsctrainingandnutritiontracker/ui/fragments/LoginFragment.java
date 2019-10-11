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
import zachg.gsctrainingandnutritiontracker.handlers.EventHandlers;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.LoginViewModel;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private User user = new User();
    LoginViewModel loginViewModel = new LoginViewModel();
    Boolean isLoggedIn;

    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        // Gets ViewModel instance to observe its LiveData
        loginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
        binding.setModel(loginViewModel);
        loginViewModel.init();

        // Bind User
        binding.setUser(user);

        // Binds EventHandler for onClick events
        EventHandlers handlers = new EventHandlers();
        binding.setHandlers(handlers);

        final Observer<Boolean> isLoggedInObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                if (true) {
                    isLoggedIn = true;
                    if (user.getIsAdmin()) {
                        goToAdminList();
                    } else if (!user.getIsAdmin()){
                        goToProfile();
                    }
                }
            }
        };

        final Observer<User> userObserver = new Observer<User>() {
            @Override
            public void onChanged(@Nullable final User newUser) {
                user = newUser;
                isLoggedIn = true;
                Log.d("plum", "user changed");
            }
        };

        return v;
    }

    public void goToRegister() {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new RegisterFragment()).addToBackStack(null).commit();
    }

    public void goToProfile() {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ClientProfileFragment(user)).addToBackStack(null).commit();
    }

    public void goToAdminList() {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new AdminUserListFragment()).addToBackStack(null).commit();
    }
}