package zachg.gsctrainingandnutritiontracker;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import zachg.gsctrainingandnutritiontracker.login.RegisterFragment;

// TODO: Get ListActivity to show menu

public class ListActivity extends AppCompatActivity {

    private RecyclerView mUserRecyclerView;
    private UserViewModel mUserViewModel;
    private Button mAddNewClient;
    private List<User> mUsers;
    private ListFragment.Callbacks mCallbacks;
    public static final int NEW_USER_ACTIVITY_REQUEST_CODE = 1;

    public void onClick(View v) {
        // required onClick method
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setHasOptionsMenu(true);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        final UserListAdapter adapter = new UserListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get a new or existing ViewModel from the ViewModelProvider.
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mUserViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable final List<User> users) {
                // Update the cached copy of the words in the adapter.
                adapter.setUsers(users);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_client:
                SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                        new RegisterFragment()).addToBackStack(null).commit();
                return true;
        }
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_USER_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            User user = new User(data.getStringExtra(RegisterFragment.EXTRA_REPLY));
            mUserViewModel.insert(user);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "not_saved",
                    Toast.LENGTH_LONG).show();
        }
    }
}