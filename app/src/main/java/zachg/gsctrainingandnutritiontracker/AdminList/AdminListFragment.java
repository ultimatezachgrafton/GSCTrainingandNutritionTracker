package zachg.gsctrainingandnutritiontracker.AdminList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.ClientProfileFragment;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.User;
import zachg.gsctrainingandnutritiontracker.inbox.InboxFragment;
import zachg.gsctrainingandnutritiontracker.login.LoginFragment;
import zachg.gsctrainingandnutritiontracker.login.RegisterFragment;

// ListFragment is the fragment that displays the list of Users which the admin accesses upon logging in
public class AdminListFragment extends Fragment implements UserListAdapter.OnItemClickListener {
    private RecyclerView mUserRecyclerView;
    private UserListAdapter adapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static ArrayList<User> mUsers = new ArrayList<>();
    public static User currentSelectedUser;

    public AdminListFragment() {}

    static {
        FirebaseFirestore.setLoggingEnabled(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_list, container, false);

        // fetch Users for display
        UserHandler.fetchUsers(mUsers);

        adapter = new UserListAdapter(UserHandler.getUserOptions(UserHandler.userColRef));

        mUserRecyclerView = v.findViewById(R.id.rvUser);
        mUserRecyclerView.setHasFixedSize(true);
        mUserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUserRecyclerView.setAdapter(adapter);

        // Click on User name in RecyclerView item, go to their profile
        if (mUsers != null) {
            adapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot doc, int position) {
                    getUserAtPosition(position);
                    String clientId = currentSelectedUser.getId();
                    SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                            new ClientProfileFragment()).addToBackStack(null).commit();
                }
            });
        }
        return v;
    }

    public User getUserAtPosition(int position) {
        currentSelectedUser = mUsers.get(position);
        Log.d("currentSelectedUser", String.valueOf(currentSelectedUser));
        return currentSelectedUser;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.admin_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bAddNewClient:
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new RegisterFragment()).addToBackStack(null).commit();
                return true;
            case R.id.bInbox:
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new InboxFragment()).addToBackStack(null).commit();
                return true;
            case R.id.bLogout:
                mAuth.signOut();
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new LoginFragment()).addToBackStack(null).commit();
                Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
                return true;
        } return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(DocumentSnapshot doc, int position) {
        // go to client's datepicker or profile
    }
}