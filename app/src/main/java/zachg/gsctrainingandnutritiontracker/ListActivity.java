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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import zachg.gsctrainingandnutritiontracker.login.RegisterFragment;

import static android.view.View.GONE;

// TODO: Get ListActivity to show menu

public class ListActivity extends AppCompatActivity {

    protected Fragment createFragment() {
        return new ListFragment();
    }

    private RecyclerView mUserRecyclerView;
    private UserViewModel mUserViewModel;
    private Button mAddNewClient;
    private List<User> mUsers;
    private ListFragment.Callbacks mCallbacks;
    public static final int NEW_USER_ACTIVITY_REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        SingleFragmentActivity.fm = getSupportFragmentManager();
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ListFragment()).addToBackStack(null).commit();

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
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.fragment_list,
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