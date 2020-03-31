package zachg.gsctrainingandnutritiontracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentRegisterBinding;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.RegisterViewModel;

public class RegisterFragment extends Fragment {

    public RegisterFragment() {}

    private String firstName, lastName, phoneNumber, email, password, confirmPassword;
    private boolean isUserCopied, isInputValid;

    private FragmentRegisterBinding binding;
    private RegisterViewModel registerViewModel = new RegisterViewModel();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate and bind the layout for this fragment_report_list
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        // Bind this fragment_report_list
        binding.setFragment(this);

        // Bind Strings
        binding.setFirstName(firstName);
        binding.setLastName(lastName);
        binding.setPhoneNumber(phoneNumber);
        binding.setEmail(email);
        binding.setPassword(password);
        binding.setConfirmPassword(confirmPassword);

        // Bind ViewModel
        binding.setViewModel(registerViewModel);
        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        registerViewModel.init();

        registerViewModel.isUserInputValid.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                if (bool) {
                    onValidatedInput(firstName, lastName, phoneNumber, email, password);
                }
            }
        });

        registerViewModel.isRegistrationComplete.observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged (Boolean bool) {
                    if (bool) {
                        returnToLogin();
                    }
                }
        });

        registerViewModel.onError.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        registerViewModel.onSuccess.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }

    public void onRegisterClick(String firstName, String lastName, String phoneNumber, String email, String password, String confirmPassword) {
        this.firstName = firstName; this.lastName = lastName; this.phoneNumber = phoneNumber;
        this.email = email; this.password = password; this.confirmPassword = confirmPassword;
        registerViewModel.registerUserCheck(firstName, lastName, phoneNumber, email, password, confirmPassword);
    }

    private void onValidatedInput(String firstName, String lastName, String phoneNumber, String email, String password) {
        registerViewModel.setUserValues(firstName, lastName, phoneNumber, email, password);
    }

    private void returnToLogin() {
        registerViewModel.isRegistrationComplete.removeObservers(this);
        registerViewModel.isUserInputValid.removeObservers(this);
        registerViewModel.onError.removeObservers(this);
        registerViewModel.onSuccess.removeObservers(this);
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new LoginFragment()).addToBackStack(null).commit();
    }

}