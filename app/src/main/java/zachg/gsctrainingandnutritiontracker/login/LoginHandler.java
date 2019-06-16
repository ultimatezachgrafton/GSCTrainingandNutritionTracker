package zachg.gsctrainingandnutritiontracker.login;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import zachg.gsctrainingandnutritiontracker.DatePickerFragment;
import zachg.gsctrainingandnutritiontracker.ListFragment;
import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.User;

public class LoginHandler {

    public void onLogin(String email) {
        // Fetch user data, then Query for email
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userColRef = db.collection("users");
        // Queries for the user associated with the entered email
        Query userQuery = userColRef.whereEqualTo("email", email);

        userQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d("Zach rules", "Query completed");
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    User currentUser = doc.toObject(User.class);
                    // Determine access level and redirect as appropriate
                    if (currentUser.getIsAdmin() == false) {
                        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                                new DatePickerFragment()).addToBackStack(null).commit();
                    } else {
                        SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                                new ListFragment()).addToBackStack(null).commit();
                    }
                }
            }
        });
    }
}
