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

import zachg.gsctrainingandnutritiontracker.DatePickerFragment;
import zachg.gsctrainingandnutritiontracker.ListFragment;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.User;
import zachg.gsctrainingandnutritiontracker.UserRoomDatabase;

// TODO: authenticate/validate

public class LoginFragment extends Fragment implements View.OnClickListener {

    private Button bLogin;
    private EditText etUsername, etPassword;
    UserRoomDatabase sUserDatabase;

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
        bLogin = (Button) view.findViewById(R.id.bLogin);
        bLogin.setOnClickListener(this);

        sUserDatabase = Room.databaseBuilder(getActivity(), UserRoomDatabase.class, "users")
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();

        //checks if Ben exists as Admin, adds if not
        //set admin
        if (sUserDatabase.userDao().getUserByName("b") == null) {
            //Admin admin = new Admin("b", "d");
            //sUserDatabase.userDao().createAdmin(admin);
            Toast.makeText(getActivity(), "hi dickon!", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.bLogin:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                //authenticate
                //if (sUserDatabase != null) {
                //    sUserDatabase.myDao();
                //authenticate(user);
                //}

                // Admin login
                if (username.equals("b") && password.equals("d")) {
                    //Admin currentUser = new Admin(username, password);
                    //currentUser.setIsLoggedIn(true);
                    SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                            new ListFragment()).addToBackStack(null).commit();
                    //Toast.makeText(getActivity(), "getting admin" + currentUser.getClientName(), Toast.LENGTH_SHORT).show();
                }
                // User login
                else if (sUserDatabase.userDao().getUserByName(username) != null) {
                        User currentUser = new User(username, password);
                        currentUser.setIsLoggedIn(true);
                        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                                new DatePickerFragment()).addToBackStack(null).commit();
                        Toast.makeText(getActivity(), "getting user" + currentUser.getClientName(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /*
    private void authenticate(User user) {
        ServerRequests serverRequest = new ServerRequests(this);
        serverRequest.fetchUserDataAsyncTask(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                    showErrorMessage();
                } else {
                    logUserIn(returnedUser);
                }
            }
        });
    }
    */
}