package zachg.gsctrainingandnutritiontracker.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.User;
import zachg.gsctrainingandnutritiontracker.UserRoomDatabase;

// TODO: authenticaTE/VALIDATE

public class RegisterFragment extends Fragment implements View.OnClickListener {

    public RegisterFragment() {
        // Required empty constructor
    }

    public static Button bRegister;
    EditText etPassword, etConfirmPassword, etClientName;
    public static UserRoomDatabase sUserDatabase;
    public static final String EXTRA_REPLY = "zachg.bensfitnessapp.REPLY";

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

                    User registeredData = new User(clientName, password);
                    RegisterFragment.sUserDatabase.userDao().addUser(registeredData);
                    Toast.makeText(getActivity(), "You are registered " + sUserDatabase.userDao().getUserByName(registeredData.getClientName()), Toast.LENGTH_SHORT).show();

                    //go back to login where this is true:
                    etClientName.setText(clientName);
                    etPassword.setText(password);
                } else {
                    Toast.makeText(getActivity(), "Passwords do not match. " + password + " " + confirmPassword,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}