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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;

public class FirestoreRepository {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private static FirestoreRepository instance;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private OnCompleteListener<QuerySnapshot> querySnapshotOnCompleteListener;
    private OnCompleteListener<DocumentSnapshot> docSnapshotOnCompleteListener;
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

    public void setQuerySnapshotOnCompleteListener(OnCompleteListener<QuerySnapshot> snapshotOnCompleteListener) {
        this.querySnapshotOnCompleteListener = snapshotOnCompleteListener;
    }


    public void setDocSnapshotOnCompleteListener(OnCompleteListener<DocumentSnapshot> snapshotOnCompleteListener) {
        this.docSnapshotOnCompleteListener = snapshotOnCompleteListener;
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
    public User getFUserByEmail(String email) {
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

    public void getUserByEmail(String email) {
        Query userQuery = userColRef.whereEqualTo("email", email);
        userQuery.get().addOnCompleteListener(querySnapshotOnCompleteListener);
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
        userQuery.get().addOnCompleteListener(querySnapshotOnCompleteListener);
    }

    // Validate that registered User's email is not currently in use
    public void duplicateEmailCheck(String email) {
        Query userQuery = userColRef.whereEqualTo("email", email);
        userQuery.get().addOnCompleteListener(querySnapshotOnCompleteListener);
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
        reportQuery.get().addOnCompleteListener(querySnapshotOnCompleteListener);
    }

    // Returns Workouts as assigned by admin
    public FirestoreRecyclerOptions<Workout> getWorkoutsFromRepo(User user) {
        Query workoutQuery = userColRef.document(user.getEmail()).collection("workouts");
        return new FirestoreRecyclerOptions.Builder<Workout>()
                .setQuery(workoutQuery, Workout.class)
                .build();
    }

    public void getWorkoutsForReport(User user) {
        Query workoutQuery = userColRef.document(user.getEmail()).collection("workouts");
        workoutQuery.get().addOnCompleteListener(querySnapshotOnCompleteListener);
    }

    public boolean checkDuplicateWorkoutTitles(User user, Workout workout) {
        Query workoutQuery = userColRef.document(user.getEmail()).collection("workouts")
                .whereEqualTo("workoutTitle", workout.getWorkoutTitle());
        // on complete, return boolean
        return false;
    }

    public FirestoreRecyclerOptions<Workout> getExercisesFromRepo(User user, Workout workout) {
        Query workoutQuery = userColRef.document(user.getEmail()).collection("workouts")
                .whereEqualTo("workoutTitle", workout.getWorkoutTitle());
        return new FirestoreRecyclerOptions.Builder<Workout>()
                .setQuery(workoutQuery, Workout.class)
                .build();
    }

    public void getExercisesForIteration(User user, int num) {
        Query exerciseQuery = userColRef.document(user.getEmail()).collection("exercises")
                .whereEqualTo("workoutDay", num);
        exerciseQuery.get().addOnCompleteListener(querySnapshotOnCompleteListener);
    }

    // Add FirebaseUser to database, returns error or success message
    public void registerFirebaseUser(User user) {
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                           @Override
                                           public void onComplete(@NonNull Task<AuthResult> task) {
                                               if (task.isSuccessful()) {
                                                   // Sign in success, update UI with the signed-in user's information
                                                   Log.d(TAG, "createUserWithEmail:success");
                                                   FirebaseUser user = auth.getCurrentUser();
                                               }
                                           }
                                       });
        registerUser(user);
    }

    public void registerUser(User user) {
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

    public void updateWorkout(User user, Workout workout) {
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

    public void deleteWorkout(User user, Workout workout) {
        db.collection("users").document(user.getEmail()).collection("workouts")
                .document(workout.getWorkoutTitle())
                .delete()
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
        report.setFullReport(report.convertToFullReport(report));
        Log.d(TAG, "exStr3: " + report.getExerciseString());
        Log.d(TAG, "fullReport: " + report.getFullReport());

        db.collection("users").document(report.getEmail()).collection("reports")
                .document(report.getDateString())
                .set(report)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: ");
                        Log.d(TAG, report.getFullReport());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
        Log.d(TAG, report.getDateString() + report.getEmail());
    }
}