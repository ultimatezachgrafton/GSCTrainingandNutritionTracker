package zachg.gsctrainingandnutritiontracker.ViewModel;

import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.Model.User;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class UserHandler {
    // TODO: where should my db be initialized? here or separately, like in a SetupHandler?
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public final CollectionReference userColRef = db.collection("users");
    public Query userQuery = userColRef;

    public ArrayList<User> fetchUsers(final ArrayList<User> users) {

        getUserOptions(userQuery);

        userQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("mReports", "hi userQuery");
                    User user = new User();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Log.d("mReports", "bi userQuery");
                        user = doc.toObject(User.class);
                        users.add(user);
                        Log.d("mReports", "user: "+user);
                        Log.d("mReports", "mUsers: " + String.valueOf(users));
                    }
                } else {
                    Log.e(TAG, "failed to make mUsers");
                }
            }
        });
        Log.d("mReports", "mUsers: " + String.valueOf(users));
        return users;
    }

    public FirestoreRecyclerOptions<User> getUserOptions(Query query) {
        // Build the database
        FirestoreRecyclerOptions<User> userOptions = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        return userOptions;
    }
}
