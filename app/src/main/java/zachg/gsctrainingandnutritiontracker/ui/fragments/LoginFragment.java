package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentLoginBinding;
import zachg.gsctrainingandnutritiontracker.handlers.EventHandlers;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.LoginViewModel;

import static androidx.databinding.library.baseAdapters.BR.email;
import static androidx.databinding.library.baseAdapters.BR.etEmail;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private User user = new User();
    LoginViewModel loginViewModel = new LoginViewModel();

    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        // Gets ViewModel instance to observe its LiveData
        binding.setModel(loginViewModel);
        loginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
        loginViewModel.init();

        // Bind this fragment
        binding.setFragment(this);

        // Bind User
        binding.setUser(user);

        // Binds EventHandler for onClick events
        EventHandlers handlers = new EventHandlers();
        binding.setHandlers(handlers);

        final Observer<Boolean> isLoggedInObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                if (true) {
                    if (user.getIsAdmin()) {
                        goToAdminList();
                    } else if (!user.getIsAdmin()){
                        goToProfile();
                    }
                }
            }
        };

//        loginViewModel.getUser().observe(getActivity(), new Observer<User>() {
//            @Override
//            public void onChanged(@Nullable User newUser) {
//                user = newUser;
//                isLoggedIn = true;
//                Log.d("plum", "user changed");
//            }
//        });


        return v;
    }

    public void onLoginClick(String e, String p) {
        Log.d("plum", binding.getUser().getEmail() + binding.getUser().getPassword());
    }

    public void onRegisterClick() {
        Log.d("plum", "eat it");
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