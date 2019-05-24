package zachg.gsctrainingandnutritiontracker.login;

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
import androidx.room.Room;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.Report;
import zachg.gsctrainingandnutritiontracker.User;
import zachg.gsctrainingandnutritiontracker.UserRoomDatabase;

import static android.content.ContentValues.TAG;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    public RegisterFragment() {
        // Required empty constructor
    }

    public static Button bRegister;
    EditText etPassword, etConfirmPassword, etClientName;
    public static UserRoomDatabase sUserDatabase;
    public static final String EXTRA_REPLY = "zachg.bensfitnessapp.REPLY";
    private Report mReport;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReport = new Report();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate layout for fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        bRegister = view.findViewById(R.id.bRegister);
        etClientName = view.findViewById(R.id.etClientName);

        bRegister.setOnClickListener(this);

        sUserDatabase = Room.databaseBuilder(getActivity(), UserRoomDatabase.class, "users").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bRegister:
                String clientName = etClientName.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();

                if (password.equals(confirmPassword)) {
                    // Access a Cloud Firestore instance
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Create a new user with a first and last name
                    Map<String, Object> user = new HashMap<>();
                    user.put("first", clientName);
                    user.put("password", password);

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

                } else {
                    // alert user if passwords do not match
                    Toast.makeText(getActivity(), "Passwords do not match. " + password + " " + confirmPassword,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}