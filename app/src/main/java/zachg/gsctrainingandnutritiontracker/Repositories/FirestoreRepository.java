package zachg.gsctrainingandnutritiontracker.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.models.Message;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.ui.fragments.AdminUserListFragment;
import zachg.gsctrainingandnutritiontracker.ui.fragments.ClientProfileFragment;
import zachg.gsctrainingandnutritiontracker.ui.fragments.LoginFragment;
import zachg.gsctrainingandnutritiontracker.utils.LoginListener;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FirestoreRepository {

    private static FirestoreRepository instance;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private LoginListener loginListener = new LoginListener() {
        @Override
        public void goToProfile() {
            SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                    new ClientProfileFragment(currentUser)).addToBackStack(null).commit();
        }

        @Override
        public void goToAdminList() {
            SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                    new AdminUserListFragment()).addToBackStack(null).commit();
        }

        @Override
        public void goToLogin() {
            SingleFragmentActivity.fm.beginTransaction().replace(R.id.fragment_container,
                    new LoginFragment()).addToBackStack(null).commit();
        }
    };

    private User currentUser = new User();

    public final CollectionReference userColRef = db.collection("users");
    public Query userQuery = userColRef;
    public final CollectionReference messageColRef = db.collection("messages");
    public Query messageQuery = messageColRef.orderBy("date");

    public static FirestoreRepository getInstance(){
        if(instance == null){
            instance = new FirestoreRepository();
        }
        return instance;
    }

    public User getCurrentUser(String email) {
        // Fetches "users" from Firestore database
        CollectionReference userColRef = db.collection("users");
        // Queries the users for matching email
        Query userQuery = userColRef.whereEqualTo("email", email);

        // Assigns matching User to currentUser
        userQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    currentUser = doc.toObject(User.class);
                    // TODO: this should not be here, this method is only about getting currentUser
                    if (currentUser.getIsAdmin()) {
                        loginListener.goToAdminList();
                    } else {
                        loginListener.goToProfile();
                    }
                }
            }
        });
        return currentUser;
    }

    public FirestoreRecyclerOptions<User> getUsersFromRepo() {
        return new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(userQuery, User.class)
                .build();
    }

    public FirestoreRecyclerOptions<Report> getReportsByUser(User user) {
        CollectionReference reportColRef = userColRef.document(user.getClientName())
                .collection("reports");
        Query reportQuery = reportColRef.orderBy("date");
        return new FirestoreRecyclerOptions.Builder<Report>()
                .setQuery(reportQuery, Report.class)
                .build();
    }

    public DocumentReference getReportByDate(User user, String date) {
        DocumentReference docRef = userColRef.document(user.getClientName())
                .collection("reports").document(date);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Report report = documentSnapshot.toObject(Report.class);
            }
        });
        return docRef;
    }

    //TODO: clientName needs to be ID or email
    public FirestoreRecyclerOptions<Workout> getWorkoutsFromRepo(User user) {
        CollectionReference workoutColRef = userColRef.document(user.getClientName()).collection("workouts")
                .document("exerciseSets").collection(String.valueOf(user.getDay()));
        Query workoutQuery = workoutColRef.whereEqualTo("workoutNum", user.getWorkoutNum());

        return new FirestoreRecyclerOptions.Builder<Workout>()
                .setQuery(workoutQuery, Workout.class)
                .build();
    }

    public FirestoreRecyclerOptions<Message> getMessagesFromRepo(User user) {
        CollectionReference messageColRef = userColRef.document(user.getClientName()).collection("messages");
        Query messageQuery = messageColRef.orderBy("date");
        return new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(messageQuery, Message.class)
                .build();
    }

    public boolean validate(String email) {
        Query validateQuery = userColRef.whereEqualTo("email", email);
        if (validateQuery == null) { return true; } else { return false;}
    }
}