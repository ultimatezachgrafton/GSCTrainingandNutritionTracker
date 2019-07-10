package zachg.gsctrainingandnutritiontracker.AdminList;

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

import zachg.gsctrainingandnutritiontracker.User;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class UserHandler {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static final CollectionReference userColRef = db.collection("users");

    public static ArrayList<User> fetchUsers(final ArrayList<User> mUsers) {

        Query userQuery = userColRef;
        getUserOptions(userColRef);

        userQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    User user = new User();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        user = doc.toObject(User.class);
                        mUsers.add(user);
                    }
                } else {
                    Log.e(TAG, "failed to make mUsers");
                }
            }
        });
        return mUsers;
    }

    public static FirestoreRecyclerOptions<User> getUserOptions(Query query) {
        // Build the database
        FirestoreRecyclerOptions<User> userOptions = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        return userOptions;
    }
}
