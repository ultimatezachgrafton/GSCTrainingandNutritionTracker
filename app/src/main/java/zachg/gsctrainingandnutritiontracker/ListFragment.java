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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import zachg.gsctrainingandnutritiontracker.login.RegisterFragment;

// ListFragment is the fragment that displays the list of Users which the admin accesses upon logging in
public class ListFragment extends Fragment implements View.OnClickListener {

    private RecyclerView mUserRecyclerView;
    private Button mAddNewClient;
    private UserListAdapter adapter;
    private static final CollectionReference userCol = FirebaseFirestore.getInstance().collection("users");
    static final Query sUserQuery = userCol.orderBy("firstName", Query.Direction.DESCENDING);
    private final static int BIFF = 1;
    public static final String TAG = "ListActivity";


    public ListFragment() {}

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
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mAddNewClient = view.findViewById(R.id.add_new_client);

        // Query the Firestore database
        // Order by name
        Query query = userCol;

        // Build the database
        FirestoreRecyclerOptions<User> users = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();
        adapter = new UserListAdapter(users);

        mUserRecyclerView = view.findViewById(R.id.recycler_view);
        mUserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUserRecyclerView.setAdapter(adapter);
        mUserRecyclerView.setHasFixedSize(true);

        mUserRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    mUserRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mUserRecyclerView.smoothScrollToPosition(0);
                        }
                    }, 100);
                }
            }
        });

        return view;
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
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