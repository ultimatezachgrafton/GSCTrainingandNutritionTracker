package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.viewmodels.LoginViewModel;
import zachg.gsctrainingandnutritiontracker.utils.LoginListener;

public class LoginFragment extends Fragment implements View.OnClickListener, LoginListener {

    private Button bLogin;
    private EditText etPassword, etEmail;
    private LoginListener mLoginListener;
    private LoginViewModel mLoginViewModel = new LoginViewModel(mLoginListener);
    private User mCurrentUser = new User();

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginViewModel.init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        etPassword = view.findViewById(R.id.etPassword);
        etEmail = view.findViewById(R.id.etEmail);
        bLogin = view.findViewById(R.id.bLogin);
        bLogin.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void onClick(View view) {
        final String password = etPassword.getText().toString();
        final String email = etEmail.getText().toString();
        mLoginViewModel.onClick(email, password);
    }

    public void goToProfile() {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ClientProfileFragment(mCurrentUser)).addToBackStack(null).commit();
    }

    public void goToAdminList() {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new AdminListFragment()).addToBackStack(null).commit();
    }

    @Override
    public void goToLogin() {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new LoginFragment()).addToBackStack(null).commit();
    }
}