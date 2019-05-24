package zachg.gsctrainingandnutritiontracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import zachg.gsctrainingandnutritiontracker.login.RegisterFragment;

// ListFragment is the fragment that displays the list of Users which the admin accesses upon logging in

public class ListFragment extends Fragment implements View.OnClickListener {

    private UserViewModel mUserViewModel;
    private RecyclerView mUserRecyclerView;
    private Button mAddNewClient;
    private List<User> mUsers;
    private Callbacks mCallbacks;

    private static final int BIFF = 1;

    public ListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mUsers = new ArrayList<>();

        //get Users, display users
//        User user1 = new User("Biff Tannen");
//        User user2 = new User("Marty McFly");
//        User user3 = new User("Doc Brown");
//        mUsers.add(user1);
//        mUsers.add(user2);
//        mUsers.add(user3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mUserRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mUserRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        UserListAdapter adapter = new UserListAdapter(getContext(), mUsers);
        mUserRecyclerView.setAdapter(adapter);

        mAddNewClient = view.findViewById(R.id.add_new_client);
         //Get a new or existing ViewModel from the ViewModelProvider.
            mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
            mUserViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
                public void onChanged(List<User> mUsers) {
                    UserListAdapter adapter = new UserListAdapter(getContext(), mUsers);
                    // Update the cached copy of the users in the adapter.
                    adapter.setUsers(mUsers);
                }
            });
        return view;
    }

    public interface Callbacks {
        void onUserSelected(User user);
    }

    @Override
    public void onClick(View v) {
        // required onClick method
        Intent intent = PagerActivity.newIntent(getActivity(), BIFF);
        startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.admin_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_client:
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new RegisterFragment()).addToBackStack(null).commit();
                return true;
        } return super.onOptionsItemSelected(item);
    }
}