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

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.models.Message;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FirestoreRepository {

    private static FirestoreRepository instance;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();

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

    //TODO: change clientName to ID or email
    public FirestoreRecyclerOptions<Workout> getWorkoutsFromRepo(User user) {
        CollectionReference workoutColRef = userColRef.document(user.getClientName()).collection("workouts")
                .document("exerciseSets").collection(String.valueOf(user.getWorkoutDay()));
        Query workoutQuery = workoutColRef.whereEqualTo("workoutNum", user.getWorkoutNum());

        return new FirestoreRecyclerOptions.Builder<Workout>()
                .setQuery(workoutQuery, Workout.class)
                .build();
    }

    public FirestoreRecyclerOptions<Message> getMessagesFromRepo(User user) {
        CollectionReference messageColRef = userColRef.document(user.getClientName()).collection("messages");
        Query messageQuery = messageColRef.orderBy("date");

        // TODO: if null, don't crash

        return new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(messageQuery, Message.class)
                .build();
    }

    public boolean validate(String email) {
        Query validateQuery = userColRef.whereEqualTo("email", email);
        if (validateQuery == null) { return true; } else { return false;}
    }

    public ArrayList<Workout> getWorkoutListFromRepo(User user) {
        final ArrayList<Workout> workoutList = new ArrayList<>();
        workoutList.add(new Workout());
        userColRef.document(user.getClientName()).collection("workouts")
                .document(String.valueOf(user.getWorkoutDay()))
                .collection(String.valueOf(user.getWorkoutDay()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Workout workout = document.toObject(Workout.class);
                                workout.setExerciseName(workout.getExerciseName());
                                workoutList.add(workout);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return workoutList;
    }
}