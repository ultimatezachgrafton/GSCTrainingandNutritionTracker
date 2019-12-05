package zachg.gsctrainingandnutritiontracker.repositories;

import android.content.ContentValues;
import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FirestoreRepository {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private static FirestoreRepository instance;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private OnCompleteListener<QuerySnapshot> snapshotOnCompleteListener;
    private User user = new User();
    public String TAG = "FirestoreRepository";

    public final CollectionReference userColRef = db.collection("users");
    public Query userQuery = userColRef;

    // Gets singleton instance of FirestoreRepository
    public static FirestoreRepository getInstance(){
        if(instance == null){
            instance = new FirestoreRepository();
        }
        return instance;
    }

    public void setSnapshotOnCompleteListener(OnCompleteListener<QuerySnapshot> snapshotOnCompleteListener) {
        this.snapshotOnCompleteListener = snapshotOnCompleteListener;
    }

    // Gets FirebaseUser for authentification
    public FirebaseUser getFireBaseUser() {
        if (auth.getCurrentUser() != null) {
            return auth.getCurrentUser(); // User is signed in
        } else {
            return null; // No user is signed in
        }
    }

    // Gets User by searching for email
    public User getUserByEmail(String email) {
        // Fetches "users" from Firestore database
        CollectionReference userColRef = db.collection("users");
        // Queries the users for matching email
        Query userQuery = userColRef.whereEqualTo("email", email);

        // Assigns matching User to currentUser
        userQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    user = doc.toObject(User.class);
                }
            }
        });
        return user;
    }

    // Gets all Users
    public FirestoreRecyclerOptions<User> getUsersFromRepo() {
        return new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(userQuery, User.class)
                .build();
    }

    // Fetches User data from email and password provided by user at login
    public void queryUserByEmailPassword(String email, String password) {
        Query userQuery = userColRef.whereEqualTo("email", email).whereEqualTo("password", password);
        userQuery.get().addOnCompleteListener(snapshotOnCompleteListener);
    }

    // Validate that registered User's email is not currently in use
    public void duplicateEmailCheck(String email) {
        Query userQuery = userColRef.whereEqualTo("email", email);
        userQuery.get().addOnCompleteListener(snapshotOnCompleteListener);
    }

    // Gets all Reports for a single User
    public FirestoreRecyclerOptions<Report> getReportsByUser(User user) {
        CollectionReference reportColRef = userColRef.document(user.getEmail())
                .collection("reports");
        Query reportQuery = reportColRef.orderBy("date");
        return new FirestoreRecyclerOptions.Builder<Report>()
                .setQuery(reportQuery, Report.class)
                .build();
    }

    // Gets all of a single User's reports on a specific date
    public void getReportByDate(User user, Date date) {
        Log.d(TAG, "in repo now" + date);
        Query reportQuery = userColRef.document(user.getEmail())
                .collection("reports").whereEqualTo("date", date);
        reportQuery.get().addOnCompleteListener(snapshotOnCompleteListener);
    }

    // Returns Workouts as assigned by admin
    public FirestoreRecyclerOptions<Workout> getWorkoutsFromRepo(User user) {
        Query workoutQuery = userColRef.whereEqualTo("email", user.getEmail());
        //workoutQuery.get().addOnCompleteListener(snapshotOnCompleteListener);
        return new FirestoreRecyclerOptions.Builder<Workout>()
                .setQuery(workoutQuery, Workout.class)
                .build();
    }

    public void registerUser(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Add user as a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: " + documentReference.getId()); }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(ContentValues.TAG, "Error adding document", e);
                    }
                });
    }

    public void writeWorkoutsToRepo(User user, Workout workout) {
        db.collection("users").document(user.getEmail()).collection("workouts")
                .document(String.valueOf(workout.getWorkoutTitle()))
                .set(workout)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("reports", "DocumentSnapshot added with ID: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("reports", "Error writing document", e);
                    }
                });
    }
}