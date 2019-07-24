package zachg.gsctrainingandnutritiontracker.UI.Fragments;

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

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.ViewModel.UserHandler;
import zachg.gsctrainingandnutritiontracker.Model.User;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.UI.Activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.UI.Adapters.UserListAdapter;

// ListFragment is the fragment that displays the list of Users which the admin accesses upon logging in
public class AdminListFragment extends Fragment implements UserListAdapter.OnItemClickListener {
    private RecyclerView mUserRecyclerView;
    private UserListAdapter adapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public ArrayList<User> mUsers = new ArrayList<>();
    public static User currentSelectedUser = new User();

    public AdminListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_list, container, false);

        // fetch Users for display
        UserHandler h = new UserHandler();
        mUsers = h.fetchUsers(mUsers);

        adapter = new UserListAdapter(h.getUserOptions(h.userQuery));

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
                    // get the doc's ID
                    SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                            new ClientProfileFragment()).addToBackStack(null).commit();
                }
            });
        }
        return v;
    }

    public void getUserAtPosition(int position) {
        currentSelectedUser = mUsers.get(position);
        Log.d("mReports", "currentSelectUser: " + String.valueOf(currentSelectedUser));
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