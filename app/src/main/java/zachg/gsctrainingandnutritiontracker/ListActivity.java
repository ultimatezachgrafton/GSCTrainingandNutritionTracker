package zachg.gsctrainingandnutritiontracker;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import zachg.gsctrainingandnutritiontracker.login.RegisterFragment;

public class ListActivity extends SingleFragmentActivity {

    protected Fragment createFragment() {
        return new ListFragment();
    }

    private RecyclerView mUserRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mUsers = db.collection("users");
    public static final String TAG = "ListActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        // Fragment transaction
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

}