package zachg.gsctrainingandnutritiontracker.ViewModels;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import zachg.gsctrainingandnutritiontracker.Models.User;

public class LoginViewModel {

    private LoginListener mLoginListener;
    public static User currentUser;
    public boolean isAdmin;

    public LoginViewModel() {}

    public LoginViewModel(LoginListener loginListener) {
        mLoginListener = loginListener;
    }

    public interface LoginListener {

        public void goToDatePicker();
        public void goToAdminList();

    }

    public void onLogin(String email) {
        // Fetch user data, then Query for email
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userColRef = db.collection("users");
        // Queries for the user associated with the entered email
        Query userQuery = userColRef.whereEqualTo("email", email);

        // TODO: goes in Repository
        userQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    currentUser = doc.toObject(User.class);
                    // Determine access level and redirect as appropriate
                    if (currentUser.getIsAdmin() == false) {
                        mLoginListener.goToDatePicker();
                        isAdmin = false;
                    } else {
                        mLoginListener.goToAdminList();
                        isAdmin = true;
                    }
                }
            }
        });
    }

}
