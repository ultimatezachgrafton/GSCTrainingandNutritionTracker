package zachg.gsctrainingandnutritiontracker.ui.fragments;

import android.os.Bundle;
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

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.ui.adapters.UserListAdapter;
import zachg.gsctrainingandnutritiontracker.ui.fragments.InboxFragment;
import zachg.gsctrainingandnutritiontracker.ui.fragments.LoginFragment;
import zachg.gsctrainingandnutritiontracker.ui.fragments.RegisterFragment;
import zachg.gsctrainingandnutritiontracker.viewmodels.AdminListViewModel;
import zachg.gsctrainingandnutritiontracker.ui.fragments.ClientProfileFragment;

// AdminListFragment displays the list of Users which the admin accesses upon logging in

public class AdminListFragment extends Fragment implements UserListAdapter.OnItemClickListener {
    private RecyclerView mUserRecyclerView;
    private AdminListViewModel mAdminListViewModel;
    private UserListAdapter mUserListAdapter;

    private User mCurrentUser = new User();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirestoreRepository mRepo = new FirestoreRepository();

    public AdminListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_list, container, false);
        // TODO: super necessary or in singlefragmentactivity?
        // super.onCreateView(inflater, container, savedInstanceState);

        mAdminListViewModel = ViewModelProviders.of(getActivity()).get(AdminListViewModel.class);

        mAdminListViewModel.init();

        mAdminListViewModel.getUsers().observe(this, new Observer<FirestoreRecyclerOptions<User>>() {
            @Override
            public void onChanged(@Nullable FirestoreRecyclerOptions<User> mUsers) {
                mUserListAdapter.notifyDataSetChanged();
            }
        });

        mAdminListViewModel.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
//                if (!aBoolean) {
//                    mUserRecyclerView.smoothScrollToPosition(mAdminListViewModel.getUsers().getValue().getSize() - 1);
//                }
            }

        });

        initRecyclerView(v);

        return v;
    }

    private void initRecyclerView(View v) {

        FirestoreRecyclerOptions<User> mUserOptions = mRepo.getUsersFromRepo();
        mUserListAdapter = new UserListAdapter(mUserOptions);

        mUserRecyclerView = v.findViewById(R.id.rvUser);
        mUserRecyclerView.setHasFixedSize(true);
        mUserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUserRecyclerView.setAdapter(mUserListAdapter);

        // Click on User name in RecyclerView item, go to their profile
        if (mCurrentUser != null) {
            mUserListAdapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot doc, int position) {
                    mUserListAdapter.getUserAtPosition(mCurrentUser);
                    // get the doc's ID
                    SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                            new ClientProfileFragment(mCurrentUser)).addToBackStack(null).commit();
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mUserListAdapter.startListening();
    }

    public void onStop() {
        super.onStop();
        mUserListAdapter.stopListening();
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
        // go to client's profile
    }

}