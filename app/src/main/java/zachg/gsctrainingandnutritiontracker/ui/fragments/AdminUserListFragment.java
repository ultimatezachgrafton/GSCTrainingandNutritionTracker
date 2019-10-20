package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import zachg.gsctrainingandnutritiontracker.databinding.FragmentAdminUserListBinding;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.ui.adapters.UserListAdapter;
import zachg.gsctrainingandnutritiontracker.viewmodels.AdminUserListViewModel;

// AdminUserListFragment displays the list of Users which the admin accesses upon logging in

public class AdminUserListFragment extends Fragment {

    private FragmentAdminUserListBinding binding;
    private RecyclerView userRecyclerView;
    private AdminUserListViewModel adminListViewModel;
    private UserListAdapter userListAdapter;

    private User currentUser = new User();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public AdminUserListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminUserListBinding.inflate(inflater, container, false);
        View v = binding.getRoot();
        adminListViewModel = ViewModelProviders.of(getActivity()).get(AdminUserListViewModel.class);

        adminListViewModel.init();

        adminListViewModel.getUsers().observe(this, new Observer<FirestoreRecyclerOptions<User>>() {
            @Override
            public void onChanged(@Nullable FirestoreRecyclerOptions<User> users) {
                initRecyclerView(users);
                userListAdapter.startListening();
            }
        });

        adminListViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (!aBoolean) {
                    userRecyclerView.smoothScrollToPosition(adminListViewModel.getUsers().getValue().getSnapshots().size() - 1);
                }
            }

        });

        return v;
    }

    private void initRecyclerView(final FirestoreRecyclerOptions<User> users) {

        userListAdapter = new UserListAdapter(users);
        binding.setAdapter(userListAdapter);

//        // Click on User name in RecyclerView item, go to their profile
//        userListAdapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
//            public void onItemClick(DocumentSnapshot doc, int position) {
//                currentUser = userListAdapter.getUserAtPosition(users.getSnapshots().get(position));
//                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
//                        new AdminClientProfileFragment(currentUser)).addToBackStack(null).commit();
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
        userListAdapter.stopListening();
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
                auth.signOut();
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new LoginFragment()).addToBackStack(null).commit();
                Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
                return true;
        } return super.onOptionsItemSelected(item);
    }

    public void onItemClick(DocumentSnapshot doc, int position) {
        // go to client's profile
    }

}