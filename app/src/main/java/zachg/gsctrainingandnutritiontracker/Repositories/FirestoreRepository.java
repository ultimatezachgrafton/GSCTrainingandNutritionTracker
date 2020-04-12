package zachg.gsctrainingandnutritiontracker.repositories;

import android.content.ContentValues;
import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.FirebaseFirestoreSource;
import zachg.gsctrainingandnutritiontracker.models.FirebaseSource;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;

// Repository to speak directly to the Firebase database
public class FirestoreRepository {

    private FirebaseSource firebaseSource = new FirebaseSource();
    private FirebaseFirestoreSource firestoreSource = new FirebaseFirestoreSource();
    private OnCompleteListener<QuerySnapshot> querySnapshotOnCompleteListener;
    private OnCompleteListener<DocumentSnapshot> documentSnapshotOnCompleteListener;
    private User user = new User();
    public String TAG = "FirestoreRepository";

    public void setQuerySnapshotOnCompleteListener(OnCompleteListener<QuerySnapshot> snapshotOnCompleteListener) {
        this.querySnapshotOnCompleteListener = snapshotOnCompleteListener;
    }

    public void setDocumentReferenceOnCompleteListener(OnCompleteListener<DocumentSnapshot> documentSnapshoteOnCompleteListener) {
        this.documentSnapshotOnCompleteListener = documentSnapshoteOnCompleteListener;
    }

    // Gets FirebaseUser for authentification
    public FirebaseUser getFirebaseUser() {
        return firebaseSource.getCurrentUser();
    }

    public void signIn(String email, String password) {
        firebaseSource.login(email, password);
    }

    public void signOut() {
        firebaseSource.logout();
    }

    // Gets User object by searching for email
    public void loginWithEmail(String email) {
        // Fetches "users" from Firestore database
        CollectionReference userColRef = firestoreSource.db.collection("users");
        // Queries the users for matching email
        Query userQuery = userColRef.whereEqualTo("email", email);
        // Assigns matching User to currentUser
        userQuery.get().addOnCompleteListener(querySnapshotOnCompleteListener);
    }

    // Gets all Users
    public FirestoreRecyclerOptions<User> getUsersFromRepo() {
        // Fetches "users" from Firestore database
        CollectionReference userColRef = firestoreSource.db.collection("users");
        Query userQuery = userColRef;
        return new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(userQuery, User.class)
                .build();
    }

    // Fetches User data from email and password provided by user at login
    public void queryUserByEmailPassword(String email, String password) {
        // Fetches "users" from Firestore database
        CollectionReference userColRef = firestoreSource.db.collection("users");
        Query userQuery = userColRef.whereEqualTo("email", email).whereEqualTo("password", password);
        userQuery.get().addOnCompleteListener(querySnapshotOnCompleteListener);
    }

    // Validate that registered User's email is not currently in use
    public void duplicateEmailCheck(String email) {
        // Fetches "users" from Firestore database
        CollectionReference userColRef = firestoreSource.db.collection("users");
        Query userQuery = userColRef.whereEqualTo("email", email);
        userQuery.get().addOnCompleteListener(querySnapshotOnCompleteListener);
    }

    public void duplicateWorkoutTitleCheck(User user, String workoutTitle) {
        // Fetches "users" from Firestore database
        CollectionReference userColRef = firestoreSource.db.collection("users");
        Query userQuery = userColRef.document(user.getEmail()).collection("workouts")
                .whereEqualTo("workoutTitle", workoutTitle);
        userQuery.get().addOnCompleteListener(querySnapshotOnCompleteListener);
    }

    // Gets all Reports for a single User
    public FirestoreRecyclerOptions<Report> getReportsByUser(User user, String dateString) {
        // Fetches "users" from Firestore database
        CollectionReference userColRef = firestoreSource.db.collection("users");
        Query reportQuery = userColRef.document(user.getEmail()).collection("reports")
                .whereEqualTo(dateString, "dateString");
        return new FirestoreRecyclerOptions.Builder<Report>()
                .setQuery(reportQuery, Report.class)
                .build();
    }

    // Gets all Reports for a single User - querySnapshot version
    public void getReportForPortal(User user, String dateString) {
        // Fetches "users" from Firestore database
        CollectionReference userColRef = firestoreSource.db.collection("users");
        DocumentReference reportDocRef = userColRef.document(user.getEmail()).collection("reports")
                .document(dateString);
        reportDocRef.get().addOnCompleteListener(documentSnapshotOnCompleteListener);
    }

    // Returns Workouts as assigned by admin
    public FirestoreRecyclerOptions<Workout> getWorkoutsFromRepo(User user) {
        // Fetches "users" from Firestore database
        CollectionReference userColRef = firestoreSource.db.collection("users");
        Query workoutQuery = userColRef.document(user.getEmail()).collection("workouts");
        return new FirestoreRecyclerOptions.Builder<Workout>()
                .setQuery(workoutQuery, Workout.class)
                .build();
    }

    // Returns Workouts as assigned by admin
    public void getExercisesForReport(User user, Report report) {
        // Fetches "users" from Firestore database
        CollectionReference userColRef = firestoreSource.db.collection("users");
        Query exerciseQuery = userColRef.document(user.getEmail()).collection("workouts")
                .document(report.getWorkoutTitle()).collection("exercises");
        exerciseQuery.get().addOnCompleteListener(querySnapshotOnCompleteListener);
    }

    public void getWorkoutsForReport(User user) {
        // Fetches "users" from Firestore database
        CollectionReference userColRef = firestoreSource.db.collection("users");
        Query workoutQuery = userColRef.document(user.getEmail()).collection("workouts");
        workoutQuery.get().addOnCompleteListener(querySnapshotOnCompleteListener);
    }

    public boolean checkDuplicateWorkoutTitles(User user, Workout workout) {
        // Fetches "users" from Firestore database
        CollectionReference userColRef = firestoreSource.db.collection("users");
        Query workoutQuery = userColRef.document(user.getEmail()).collection("workouts")
                .whereEqualTo("workoutTitle", workout.getWorkoutTitle());
        // on complete, return boolean
        return false;
    }

    public FirestoreRecyclerOptions<Exercise> getExercisesFromRepo(User user, Workout workout) {
        // Fetches "users" from Firestore database
        CollectionReference userColRef = firestoreSource.db.collection("users");
        Query exerciseQuery = userColRef.document(user.getEmail()).collection("workouts")
                .document(workout.getWorkoutTitle()).collection("exercises");
        return new FirestoreRecyclerOptions.Builder<Exercise>()
                .setQuery(exerciseQuery, Exercise.class)
                .build();
    }

    public void getExercisesForArray(User user, Workout workout) {
        // Fetches "users" from Firestore database
        CollectionReference userColRef = firestoreSource.db.collection("users");
        Query exerciseQuery = userColRef.document(user.getEmail()).collection("workouts")
                .document(workout.getWorkoutTitle()).collection("exercises");
        exerciseQuery.get().addOnCompleteListener(querySnapshotOnCompleteListener);
    }

    public FirestoreRecyclerOptions<Exercise> getExercisesFromRepo(User user, Report report) {
        // Fetches "users" from Firestore database
        CollectionReference userColRef = firestoreSource.db.collection("users");
        Query exerciseQuery = userColRef.document(user.getEmail()).collection("reports")
                .document(report.getDateString()).collection("exercises");
        return new FirestoreRecyclerOptions.Builder<Exercise>()
                .setQuery(exerciseQuery, Exercise.class)
                .build();
    }

    public FirestoreRecyclerOptions<Report> getReportsFromRepo(User user) {
        // Fetches "users" from Firestore database
        CollectionReference userColRef = firestoreSource.db.collection("users");
        Query reportQuery = userColRef.document(user.getEmail()).collection("reports")
                .orderBy("dateString", Query.Direction.ASCENDING);
        return new FirestoreRecyclerOptions.Builder<Report>()
                .setQuery(reportQuery, Report.class)
                .build();
    }

    public void createBlankExercises(User user, Workout workout, Exercise exercise) {
        firestoreSource.db.collection("users").document(user.getEmail()).collection("workouts")
                .document(workout.getWorkoutTitle()).collection("exercises").document(exercise.getId())
                .set(exercise)
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

    // Add FirebaseUser to database, returns error or success message
    public void registerFirebaseUser(User user) {
        firebaseSource.register(user.getEmail(), user.getPassword());
        addUserToDatabase(user);
    }

    // Store user's data in a User model object stored in the database
    public void addUserToDatabase(User user) {
        // Add user as a new document with a generated ID
        firestoreSource.db.collection("users").document(user.getEmail())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ");
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
        firestoreSource.db.collection("users").document(user.getEmail()).collection("workouts")
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
        firestoreSource.db.collection("users").document(user.getEmail()).collection("workouts")
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
        firestoreSource.db.collection("users").document(user.getEmail()).collection("workouts")
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

    public void writeReportToRepo(User user, Report report) {
        firestoreSource.db.collection("users").document(user.getEmail()).collection("reports")
                .document(report.getDateString())
                .set(report)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
        writeExercisesToReport(report);
    }

    public void writeExercisesToReport(Report report) {
        for (int i = 0; i < report.getExerciseArrayList().size(); i++) {
            firestoreSource.db.collection("users").document(report.getEmail()).collection("reports")
                    .document(report.getDateString()).collection("exercises")
                    .document(report.getExerciseArrayList().get(i).getExerciseName())
                    .set(report.getExerciseArrayList().get(i))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot added with ID: ");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
        }
    }

    public void writeExercisesToRepo(User user, Workout workout, Exercise exercise) {
        firestoreSource.db.collection("users").document(user.getEmail()).collection("workouts")
                .document(workout.getWorkoutTitle()).collection("exercises").document(exercise.getId())
                .set(exercise)
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