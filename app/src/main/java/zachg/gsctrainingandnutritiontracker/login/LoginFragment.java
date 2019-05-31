package zachg.gsctrainingandnutritiontracker.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.Query;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import zachg.gsctrainingandnutritiontracker.DatePickerFragment;
import zachg.gsctrainingandnutritiontracker.ListFragment;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.User;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private Button bLogin;
    private EditText etUsername, etPassword, etEmail;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("users");

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        etEmail = view.findViewById(R.id.etEmail);
        bLogin = (Button) view.findViewById(R.id.bLogin);
        bLogin.setOnClickListener(this);

        Query query = userRef;
        FirestoreRecyclerOptions<User> users = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        return view;
    }

    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.bLogin:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String email = etEmail.getText().toString();

                //authenticate
                //if (sUserDatabase != null) {
                //    sUserDatabase.myDao();
                //authenticate(user);
                //}

                // User login
                if (username != null) {
                        User currentUser = new User(username, email, password);
                        currentUser.setIsLoggedIn(true);
                        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                                new DatePickerFragment()).addToBackStack(null).commit();
                        Toast.makeText(getActivity(), "getting user" + currentUser.getClientName(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}