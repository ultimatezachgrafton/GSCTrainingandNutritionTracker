package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentRegisterBinding;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.AdminClientProfileViewModel;
import zachg.gsctrainingandnutritiontracker.viewmodels.RegisterViewModel;

import static android.content.ContentValues.TAG;

public class RegisterFragment extends Fragment {

    public RegisterFragment() {}

    private FragmentRegisterBinding binding;
    private RegisterViewModel registerViewModel = new RegisterViewModel();
    private User user = new User();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate and biond the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        // Bind this fragment
        binding.setFragment(this);

        // Bind user
        binding.setUser(user);

        // Bind ViewModel
        binding.setViewModel(registerViewModel);

        // Get ViewModel instance
        registerViewModel = ViewModelProviders.of(getActivity()).get(RegisterViewModel.class);

        return v;
    }

    public void onRegisterClick() {
        if (validate()) {
            if (user.getPassword() != user.getConfirmPassword()) {
                onError("Passwords entered do not match.");
                Log.d("plum", user.getPassword() + user.getConfirmPassword());
            } else if (registerViewModel.duplicateUserCheck(user.getEmail())) {
                registerViewModel.registerUser(user);
            } else {
                onError("This email is already in use.");
            }
        } else {
            onError("Please fill out all fields");
        }
    }

    public boolean validate() {
        if (user.getFirstName() != null && user.getLastName() != null && user.getEmail() != null
                && user.getPassword() != null && user.getConfirmPassword() != null) {
            return true;
        } else {
            return false;
        }
    }

    public void onError(String e) {
        Toast.makeText(getContext(), e, Toast.LENGTH_SHORT).show();
    }
}