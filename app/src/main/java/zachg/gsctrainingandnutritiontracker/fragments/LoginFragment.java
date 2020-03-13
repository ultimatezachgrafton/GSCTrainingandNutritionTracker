package zachg.gsctrainingandnutritiontracker.fragments;

import android.os.Bundle;
import android.util.Log;
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
import zachg.gsctrainingandnutritiontracker.databinding.FragmentLoginBinding;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.LoginViewModel;

public class LoginFragment extends Fragment {

    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private FragmentLoginBinding binding;
    private User user = new User();
    LoginViewModel loginViewModel = new LoginViewModel();
    String loggingIn = "Logging In...";
    String logInNull = "Please fill out all fields.";
    String userNull = "No record of user with this email/password.";
    String TAG = "LoginFragment";

    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate and bind the layout for this fragment_report_list
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        // Gets ViewModel instance to observe its LiveData
        binding.setModel(loginViewModel);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.init();

        // Bind this fragment_report_list
        binding.setFragment(this);

        // Bind User
        binding.setUser(user);

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

        loginViewModel.isLoggingIn.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                if (bool) {
                    Toast.makeText(getContext(), loggingIn, Toast.LENGTH_LONG).show();
                }
            }
        });

        loginViewModel.isLogInNull.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                if (bool) {
                    Toast.makeText(getContext(), logInNull, Toast.LENGTH_LONG).show();
                }
            }
        });

        loginViewModel.doesUserExist.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                if (!bool) {
                    Log.d(TAG, "bool");
                    Toast.makeText(getContext(), userNull, Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        firebaseUser = auth.getCurrentUser();
    }

    public void onLoginClick(final String email, final String password) {
        // Check if login values are valid
        loginViewModel.verifyUser(email, password);
    }


    // after error w registration, goes back to login and gives error D/FirestoreRepository: fuser null: null;
    // and register does not work

    public void onRegisterClick() {
        loginViewModel.currentUser.removeObservers(this);
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new RegisterFragment()).addToBackStack(null).commit();
    }

    public void goToProfile(User user) {
        Toast.makeText(getContext(), "Logging in...", Toast.LENGTH_LONG).show();
        loginViewModel.currentUser.removeObservers(this);
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ClientPortalFragment(user)).addToBackStack(null).commit();
    }

    public void goToAdminList(User user) {
        Toast.makeText(getContext(), "Logging in...", Toast.LENGTH_LONG).show();
        loginViewModel.currentUser.removeObservers(this);
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new AdminUserListFragment(user)).addToBackStack(null).commit();
    }

    public void signOut() {
        auth.signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null)
        firebaseUser = auth.getCurrentUser();
    }
}