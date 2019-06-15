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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.login.LoginFragment;
import zachg.gsctrainingandnutritiontracker.login.RegisterFragment;

// ListFragment is the fragment that displays the list of Users which the admin accesses upon logging in
public class ListFragment extends Fragment implements UserListAdapter.OnItemClickListener {

    private RecyclerView mUserRecyclerView;
    private Button mAddNewClient;
    private UserListAdapter adapter;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final CollectionReference userCol = db.collection("users");
    private static final CollectionReference reportCol = db.collection("reports");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ArrayList<Report> mReports;
    private final static int BIFF = 1;

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

        // Query the Firestore collection
        Query query = userCol;
        // Build the database
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        adapter = new UserListAdapter(options);

        mUserRecyclerView = view.findViewById(R.id.recycler_view);
        mUserRecyclerView.setHasFixedSize(true);
        mUserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUserRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
            // Fetch user info into User object
            @Override
            public void onItemClick(DocumentSnapshot doc, int position) {
                User user = doc.toObject(User.class);
                String id = doc.getId();
                String path = doc.getReference().getPath();

                // Query for Reports with same firstName, lastName as user
                Query reportQuery = reportCol.whereEqualTo("lastName", user.getLastName())
                        .whereEqualTo("firstName", user.getFirstName())
                        .orderBy("date", Query.Direction.ASCENDING);
                reportQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc: task.getResult()) {
                                Report report = doc.toObject(Report.class);
                                mReports.add(report);
                            }
                        } else {
                            Toast.makeText(getActivity(), "failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                if (mReports == null) {
                    SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                            new DatePickerFragment()).addToBackStack(null).commit();
                } else {
                    SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                            new DatePickerFragment(mReports)).addToBackStack(null).commit();
                }
            }
        });
        return view;
    }

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

    }
}