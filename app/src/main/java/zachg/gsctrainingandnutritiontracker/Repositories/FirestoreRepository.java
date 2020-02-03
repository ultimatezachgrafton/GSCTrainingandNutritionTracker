package zachg.gsctrainingandnutritiontracker.repositories;

import android.content.ContentValues;
import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;

public class FirestoreRepository {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private static FirestoreRepository instance;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private OnCompleteListener<QuerySnapshot> snapshotOnCompleteListener;
    private User user = new User();
    public String TAG = "FirestoreRepository";

    public final CollectionReference userColRef = db.collection("users");
    public Query userQuery = userColRef;
    public Query reportQuery = userColRef; // set to fetch reports

    // Gets singleton instance of FirestoreRepository
    public static FirestoreRepository getInstance() {
        if (instance == null) {
            instance = new FirestoreRepository();
        }
        return instance;
    }

    public void setSnapshotOnCompleteListener(OnCompleteListener<QuerySnapshot> snapshotOnCompleteListener) {
        this.snapshotOnCompleteListener = snapshotOnCompleteListener;
    }

    // Gets FirebaseUser for authentification
    public FirebaseUser getFirebaseUser() {
        if (auth.getCurrentUser() != null) {
            Log.d(TAG, "fuser not null: " + String.valueOf(auth.getCurrentUser()));
            return auth.getCurrentUser(); // User is signed in
        } else {
            Log.d(TAG, "fuser null: " + String.valueOf(auth.getCurrentUser()));
            return null; // No user is signed in
        }
    }

    public void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "signInWithEmail:failure", task.getException());
                        }
                    }
                });
    }

    public void signOut() {
        auth.getInstance().signOut();
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
    public FirestoreRecyclerOptions<Report> getReportsByUser(User user, String string) {
        Query reportQuery = userColRef.document(user.getEmail()).collection("reports")
                .whereEqualTo("clientName", user.getClientName());
        return new FirestoreRecyclerOptions.Builder<Report>()
                .setQuery(reportQuery, Report.class)
                .build();
//        reportQuery.get().addOnCompleteListener(snapshotOnCompleteListener);
    }

    // Gets all of a single User's reports on a specific date
    public void getReportByUser(User user) {
        // TODO: when registering user, doc id is always user.getemail

        Query reportQuery = userColRef.document(user.getEmail())
                .collection("reports");
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
        // Add FirebaseUser to database
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });

        // Store user's data in a User model object stored in the database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Add user as a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
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
                .document(workout.getWorkoutTitle())
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

    public void writeReportToRepo(Report report) {
        db.collection("users").document(report.getEmail()).collection("reports")
                .document(report.getDateString())
                .set(report)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("reports", "DocumentSnapshot added with ID: ");
                        Log.d("reports", report.getDateString() + report.getClientName());
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