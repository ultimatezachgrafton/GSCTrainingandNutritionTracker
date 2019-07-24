package zachg.gsctrainingandnutritiontracker.UI.Fragments;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.UI.Activities.SingleFragmentActivity;

import static android.content.ContentValues.TAG;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    public RegisterFragment() {
        // Required empty constructor
    }

    Button bRegister;
    EditText etPassword, etConfirmPassword, etFirstName, etLastName, etEmail, etGender, etBirthDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate layout for fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        bRegister = view.findViewById(R.id.bRegister);
        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etEmail = view.findViewById(R.id.etEmail);
        etGender = view.findViewById(R.id.etGender);
        etBirthDate = view.findViewById(R.id.etBirthDate);

        bRegister.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bRegister:
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                String gender = etGender.getText().toString();
                String birthDate = etBirthDate.getText().toString();

                if (password.equals(confirmPassword)) {
                    // AND password != an existing password

                    // Access a Cloud Firestore instance
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Create a new user
                    Map<String, Object> user = new HashMap<>();
                    user.put("firstName", firstName);
                    user.put("lastName", lastName);
                    user.put("email", email);
                    user.put("password", password);
                    user.put("gender", gender);
                    user.put("birthdate", birthDate);

                    // Add user as a new document with a generated ID
                    db.collection("users")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
                    // send user to DatePickerFragment
                    SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                            new DatePickerFragment()).addToBackStack(null).commit();

                } else {
                    // alert user if passwords do not match
                    Toast.makeText(getActivity(), "Passwords do not match. " + password + " " + confirmPassword,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}