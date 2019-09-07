package zachg.gsctrainingandnutritiontracker.repositories;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import zachg.gsctrainingandnutritiontracker.models.Message;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.utils.LoginListener;

public class FirestoreRepository {

    private static FirestoreRepository instance;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private LoginListener mLoginListener;

    private User mCurrentUser = new User();

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

        // Assigns matching User to mCurrentUser
        userQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    mCurrentUser = doc.toObject(User.class);
                    mLoginListener.goToProfile();
                }
            }
        });
        return mCurrentUser;
    }

    public FirestoreRecyclerOptions<User> getUsersFromRepo() {
        return new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(userQuery, User.class)
                .build();
    }

    public FirestoreRecyclerOptions<Report> getReportsFromRepo(User user) {
        CollectionReference reportColRef = userColRef.document(user.getClientName())
                .collection("reports");
        Query reportQuery = reportColRef.whereEqualTo("clientName", getCurrentUser(user.getEmail()))
                .orderBy("date");
        return new FirestoreRecyclerOptions.Builder<Report>()
                .setQuery(reportQuery, Report.class)
                .build();
    }

    public FirestoreRecyclerOptions<Workout> getWorkoutsFromRepo(User user) {
        CollectionReference workoutColRef = userColRef.document(user.getClientName()).collection("workouts")
                .document("exerciseSets").collection(String.valueOf(user.getWorkoutNum()));
        Query workoutQuery = workoutColRef;

        return new FirestoreRecyclerOptions.Builder<Workout>()
                .setQuery(workoutQuery, Workout.class)
                .build();
    }

    public FirestoreRecyclerOptions<Message> getMessagesFromRepo() {
        return new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(messageQuery, Message.class)
                .build();
    }
}