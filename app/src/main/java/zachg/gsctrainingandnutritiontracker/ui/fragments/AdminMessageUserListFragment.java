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

import zachg.gsctrainingandnutritiontracker.databinding.FragmentAdminMessageUserListBinding;
import zachg.gsctrainingandnutritiontracker.databinding.FragmentClientProfileBinding;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.ui.adapters.UserListAdapter;
import zachg.gsctrainingandnutritiontracker.viewmodels.AdminMessageUserListViewModel;
import zachg.gsctrainingandnutritiontracker.viewmodels.AdminUserListViewModel;

// AdminUserListFragment displays the list of Users which the admin accesses upon logging in

public class AdminMessageUserListFragment extends Fragment {

    private FragmentAdminMessageUserListBinding binding;

    private RecyclerView messageUserRecyclerView;
    private AdminMessageUserListViewModel adminMessageViewModel;
    private UserListAdapter userListAdapter;

    private User currentUser = new User();

    public AdminMessageUserListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminMessageUserListBinding.inflate(inflater, container, false);
        final View v = binding.getRoot();
        adminMessageViewModel = ViewModelProviders.of(getActivity()).get(AdminMessageUserListViewModel.class);

        adminMessageViewModel.init();

        adminMessageViewModel.getUsers().observe(this, new Observer<FirestoreRecyclerOptions<User>>() {
            @Override
            public void onChanged(@Nullable FirestoreRecyclerOptions<User> users) {
                initRecyclerView(v, users);
                userListAdapter.startListening();
            }
        });

        adminMessageViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (!aBoolean) {
                    messageUserRecyclerView.smoothScrollToPosition(adminMessageViewModel.getUsers().getValue().getSnapshots().size() - 1);
                }
            }

        });

        return v;
    }

    private void initRecyclerView(View v, final FirestoreRecyclerOptions<User> users) {

        userListAdapter = new UserListAdapter(users);
        binding.setUserListAdapter(userListAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
        userListAdapter.stopListening();
    }

    public void onItemClick(DocumentSnapshot doc, int position) {
        //currentUser = userListAdapter.getUserAtPosition(users.getSnapshots().get(position));
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new SendMessageFragment(currentUser)).addToBackStack(null).commit();
    }

}