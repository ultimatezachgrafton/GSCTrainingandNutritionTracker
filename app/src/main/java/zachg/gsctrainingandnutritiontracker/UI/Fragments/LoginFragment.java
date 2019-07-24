package zachg.gsctrainingandnutritiontracker.UI.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.UI.Activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.ViewModel.LoginHandler;
import zachg.gsctrainingandnutritiontracker.ViewModel.LoginListener;

public class LoginFragment extends Fragment implements View.OnClickListener, LoginListener {

    private FirebaseAuth mAuth;
    private Button bLogin;
    private EditText etPassword, etEmail;
    private LoginHandler mLoginHandler;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mLoginHandler = new LoginHandler(this);

        if (mAuth.getCurrentUser() != null) {
            // create user to
            // go to apropos frag
            mLoginHandler.onLogin(mAuth.getCurrentUser().getEmail());
        }
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

        // authenticate
        if (email != null && password != null) {
            mAuth.signInWithEmailAndPassword(email, password);
            // if successful...
            mLoginHandler.onLogin(email);

        } else {
            Toast.makeText(getActivity(), "Do not leave fields blank", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void goToDatePicker() {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new DatePickerFragment()).addToBackStack(null).commit();
    }

    @Override
    public void goToAdminList() {
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new AdminListFragment()).addToBackStack(null).commit();
    }
}