package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentRegisterBinding;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.RegisterViewModel;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RegisterFragment extends Fragment {

    public RegisterFragment() {}

    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;

    private FragmentRegisterBinding binding;
    private RegisterViewModel registerViewModel = new RegisterViewModel();
    private User newUser = new User();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate and bind the layout for this fragment_report_list
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        // Bind this fragment_report_list
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
                returnToLogin();
            }
        });

        // Displays error statements
        registerViewModel.onError.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        registerViewModel.isDuplicate.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    Log.d(TAG, "observer");
                    registerViewModel.registerUser();
                }
            }
        });

        return v;
    }

    public void onRegisterClick(String firstName, String lastName, String phoneNumber, String email, String password, String confirmPassword) {
        registerViewModel.registerUserCheck(firstName, lastName, phoneNumber, email, password, confirmPassword);
    }

    public void returnToLogin() {
        registerViewModel.newUser.removeObservers(this);
        registerViewModel.onError.removeObservers(this);
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new LoginFragment()).addToBackStack(null).commit();
    }

    public void registerUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }
}