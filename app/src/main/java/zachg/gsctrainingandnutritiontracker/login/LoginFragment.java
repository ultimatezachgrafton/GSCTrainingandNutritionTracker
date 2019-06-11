package zachg.gsctrainingandnutritiontracker.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import zachg.gsctrainingandnutritiontracker.DatePickerFragment;
import zachg.gsctrainingandnutritiontracker.ListFragment;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.User;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private Button bLogin;
    private DocumentSnapshot mSnap;
    private EditText etPassword, etEmail;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            // create user to
            // go to apropos frag
            onLogin(mAuth.getCurrentUser().getEmail());
        }
    }

    public void onLogin(String email) {
        // Fetch user data, then Query for email
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userColRef = db.collection("users");
        // Queries for the user associated with the entered email
        Query userQuery = userColRef.whereEqualTo("email", email);

        userQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    User currentUser = doc.toObject(User.class);
                    // Determine access level and redirect as appropriate
                    if (currentUser.getIsAdmin() == false) {
                        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                                new DatePickerFragment()).addToBackStack(null).commit();
                        Toast.makeText(getActivity(), "User login" + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
                    } else {
                        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                                new ListFragment()).addToBackStack(null).commit();
                        Toast.makeText(getActivity(), "Admin login" + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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
            onLogin(email);
        } else {
            Toast.makeText(getActivity(), "Do not leave fields blank", Toast.LENGTH_SHORT).show();
        }
    }
}