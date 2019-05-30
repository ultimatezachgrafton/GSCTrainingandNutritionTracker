package zachg.gsctrainingandnutritiontracker;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import zachg.gsctrainingandnutritiontracker.login.RegisterFragment;

public class ListActivity extends SingleFragmentActivity {

    protected Fragment createFragment() {
        return new ListFragment();
    }

    private UserViewModel mUserViewModel;
    private RecyclerView mUserRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mUsers = db.collection("users");
    public static final int NEW_USER_ACTIVITY_REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        layoutManager = new LinearLayoutManager(this);
        mUserRecyclerView.setLayoutManager(layoutManager);

        // Fragment transaction
        SingleFragmentActivity.fm = getSupportFragmentManager();
        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                new ListFragment()).addToBackStack(null).commit();

        // Query the Firestore database
        Query query = mUsers.orderBy("priority", Query.Direction.DESCENDING);

        // Build the database
        FirestoreRecyclerOptions<User> users = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        UserListAdapter adapter = new UserListAdapter(users);

        mUserRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mUserRecyclerView.setHasFixedSize(true);

        mUserRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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