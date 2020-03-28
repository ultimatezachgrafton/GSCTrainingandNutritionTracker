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
    private LoginViewModel loginViewModel = new LoginViewModel();
    private static final String loggingIn = "Logging In...";
    private static final String logInNull = "Please fill out all fields.";
    private static final String userNull = "No record of user with this email/password.";
    private static final String TAG = "LoginFragment";

    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate and bind the layout for this fragment_report_list
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        // Gets ViewModel instance to observe its LiveData
        binding.setModel(loginViewModel);
        //loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.init(firebaseUser);

        // Bind this fragment_report_list
        binding.setFragment(this);

        // Bind User
        binding.setUser(user);

        loginViewModel.firebaseUser.observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser fUser) {
                if (user == null) {
                    Toast.makeText(getContext(), "That user does not exist.", Toast.LENGTH_SHORT).show();
                } else if (user.getIsAdmin()) {
                    goToAdminList(user);
                } else if (!user.getIsAdmin()) {
                    User client = user;
                    goToProfile(user, client);
                }
            }
        });

        loginViewModel.user.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user == null) {
                    Toast.makeText(getContext(), "That user does not exist.", Toast.LENGTH_SHORT).show();
                } else if (user.getIsAdmin()) {
                    goToAdminList(user);
                } else if (!user.getIsAdmin()) {
                    User client = user;
                    goToProfile(user, client);
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

        loginViewModel.isLoggedIn.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
                if (bool) {
                    if (user.getIsAdmin()) {
                        goToAdminList(user);
                    } else if (!user.getIsAdmin()) {
                        User client = user;
                        goToProfile(user, client);
                } else if (!bool) {
                        // set all livedata to null
                        loginViewModel.clearLiveData();
                    }
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
                    Toast.makeText(getContext(), userNull, Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
    }

    public void onLoginClick(final String email, final String password) {
        // Check if login values are valid
        loginViewModel.verifyUser(email, password);
    }

    // TODO: after error w registration, goes back to login and gives error D/FirestoreRepository: fuser null: null;
    // and register does not work

    // weird xml problem

    public void onRegisterClick() {
        loginViewModel.user.removeObservers(this);
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new RegisterFragment()).addToBackStack(null).commit();
    }

    public void goToProfile(User user, User client) {
        loginViewModel.clearLiveData();
        loginViewModel.user.removeObservers(this);
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ClientPortalFragment(user, client)).addToBackStack(null).commit();
    }

    public void goToAdminList(User user) {
        loginViewModel.user.removeObservers(this);
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new AdminUserListFragment(user)).addToBackStack(null).commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null)
        firebaseUser = auth.getCurrentUser();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}