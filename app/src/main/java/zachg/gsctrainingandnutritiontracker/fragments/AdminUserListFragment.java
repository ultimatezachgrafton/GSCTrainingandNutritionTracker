package zachg.gsctrainingandnutritiontracker.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentAdminUserListBinding;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.adapters.UserListAdapter;
import zachg.gsctrainingandnutritiontracker.viewmodels.AdminUserListViewModel;

// AdminUserListFragment displays the list of Users which the admin accesses upon logging in

public class AdminUserListFragment extends Fragment {

    private FragmentAdminUserListBinding binding;
    private AdminUserListViewModel adminListViewModel;
    private UserListAdapter userListAdapter;
    public String TAG = "AdminUserListFragment";

    private User currentUser = new User();
    private User currentClient = new User();

    public AdminUserListFragment(User user) {
        this.currentUser = user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminUserListBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();
        binding.setUser(currentUser);
        binding.setClient(currentClient);

        // Gets ViewModel instance to observe  LiveData
        binding.setModel(adminListViewModel);
        adminListViewModel = ViewModelProviders.of(getActivity()).get(AdminUserListViewModel.class);
        adminListViewModel.init(currentUser);

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
                    binding.rvUser.smoothScrollToPosition(adminListViewModel.getUsers().getValue().getSnapshots().size() - 1);
                }
            }
        });

        adminListViewModel.getUserClicked().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User u) {
                currentClient = u;
                // Goes to client's profile fragment_report_list
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new AdminClientProfileFragment(currentUser, currentClient)).addToBackStack(null).commit();
            }
        });

        return v;
    }

    private void initRecyclerView(FirestoreRecyclerOptions<User> users) {
        userListAdapter = new UserListAdapter(users);
        binding.rvUser.setAdapter(userListAdapter);

        binding.rvUser.setHasFixedSize(true);
        binding.rvUser.setLayoutManager(new LinearLayoutManager(getContext()));

        userListAdapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                adminListViewModel.onItemClicked(documentSnapshot, position);
            }
        });
    }

    // todo
    public void removeObservers() {
        adminListViewModel.getUserClicked().removeObservers(this);
        adminListViewModel.getIsUpdating().removeObservers(this);
        adminListViewModel.getUsers().removeObservers(this);
    }

    public void onStop() {
        super.onStop();
        userListAdapter.stopListening();
    }
}