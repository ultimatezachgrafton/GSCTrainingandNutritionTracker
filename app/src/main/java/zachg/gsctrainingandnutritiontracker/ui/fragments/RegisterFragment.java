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

import zachg.gsctrainingandnutritiontracker.databinding.FragmentRegisterBinding;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.viewmodels.RegisterViewModel;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RegisterFragment extends Fragment {

    public RegisterFragment() {
    }

    private FragmentRegisterBinding binding;
    private RegisterViewModel registerViewModel = new RegisterViewModel();
    private User newUser = new User();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate and bind the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        // Bind this fragment
        binding.setFragment(this);

        // Bind user
        binding.setUser(newUser);

        // Bind ViewModel
        binding.setViewModel(registerViewModel);
        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        registerViewModel.init();

        // Get ViewModel instance
        registerViewModel = ViewModelProviders.of(getActivity()).get(RegisterViewModel.class);

        registerViewModel.newUser.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.d(TAG, "newUser changed");
            }
        });

        // Displays error statements
        registerViewModel.onError.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT);
            }
        });

        return v;
    }

    public void onRegisterClick(String firstName, String lastName, String email, String password, String confirmPassword) {
        Log.d(TAG, "oRC : " + firstName);

        // setValues?

        registerViewModel.registerUserCheck(firstName, lastName, email, password, confirmPassword);
    }
}