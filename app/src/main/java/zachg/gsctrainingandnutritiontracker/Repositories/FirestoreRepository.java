package zachg.gsctrainingandnutritiontracker.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.Models.Message;
import zachg.gsctrainingandnutritiontracker.Models.Report;
import zachg.gsctrainingandnutritiontracker.Models.User;
import zachg.gsctrainingandnutritiontracker.Models.Workout;

import static zachg.gsctrainingandnutritiontracker.UI.Fragments.AdminListFragment.currentSelectedUser;

public class FirestoreRepository {

    // Singleton pattern
    private static FirestoreRepository instance;

    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public final CollectionReference userColRef = db.collection("users");
    public Query userQuery = userColRef;
    public final CollectionReference reportColRef = db.collection("users")
            .document("Ben Grafton")//currentSelectedUser.getClientName())
            .collection("reports");
    public Query reportQuery = reportColRef.whereEqualTo("clientName", currentSelectedUser.getClientName())
            .orderBy("date");
    public final CollectionReference workoutColRef = db.collection("workouts");
    public Query workoutQuery = workoutColRef.whereEqualTo("clientName", currentSelectedUser.getClientName());
    public final CollectionReference messageColRef = db.collection("messages");
    public Query messageQuery = messageColRef.orderBy("date");

    // fill out workouts from workoutDays subcollection
    public static ArrayList<User> sUsers = new ArrayList<>();
    public static ArrayList<Report> sReports = new ArrayList<>();
    public static ArrayList<Workout> sWorkouts = new ArrayList<>();
    public static ArrayList<Message> sMessages = new ArrayList<>();

    public static FirestoreRepository getInstance(){
        if(instance == null){
            instance = new FirestoreRepository();
        }
        return instance;
    }

    // Get data from a webservice or online source
    public MutableLiveData<ArrayList<User>> getUsers(){
        setUsers();
        MutableLiveData<ArrayList<User>> data = new MutableLiveData<>();
        data.setValue(sUsers);
        return data;
    }

    public MutableLiveData<ArrayList<Report>> getReports() {
        setReports();
        MutableLiveData<ArrayList<Report>> data = new MutableLiveData<>();
        data.setValue(sReports);
        return data;
    }

    public MutableLiveData<ArrayList<Workout>> getWorkouts() {
        setWorkouts();
        MutableLiveData<ArrayList<Workout>> data = new MutableLiveData<>();
        data.setValue(sWorkouts);
        return data;
    }

    public MutableLiveData<ArrayList<Message>> getMessages() {
        setMessages();
        MutableLiveData<ArrayList<Message>> data = new MutableLiveData<>();
        data.setValue(sMessages);
        return data;
    }

    public FirestoreRecyclerOptions<User> setUsers() {
        // Build the database
        FirestoreRecyclerOptions<User> mUserOptions = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(userQuery, User.class)
                .build();

        userQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    User user = new User();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        user = doc.toObject(User.class);
                        sUsers.add(user);
                    }
                } else {
                    Log.d("mReports", "failed to make mUsers");
                }
            }
        });
        return mUserOptions;
    }

    public FirestoreRecyclerOptions<Report> setReports() {
        // Build the database
        FirestoreRecyclerOptions<Report> mReportOptions = new FirestoreRecyclerOptions.Builder<Report>()
                .setQuery(reportQuery, Report.class)
                .build();

        // TODO: order by date

        reportQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Report report = new Report();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        report = doc.toObject(Report.class);
                        Log.d("mReports", "report: " + String.valueOf(report));
                        sReports.add(report);
                        Log.d("mReports", "mReports creation successful");
                        Log.d("mReports", String.valueOf(sReports));
                    }
                } else {
                    Log.d("mReports", "failed to make mReports");
                }
            }
        });
        return mReportOptions;
    }

    public FirestoreRecyclerOptions<Workout> setWorkouts() {
        // Build the database
        FirestoreRecyclerOptions<Workout> mWorkoutOptions = new FirestoreRecyclerOptions.Builder<Workout>()
                .setQuery(workoutQuery, Workout.class)
                .build();

        Log.d("mReports", "workouts built");

        workoutQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                Log.d("mReports", "workouts task to be conducted:");
                if (task.isSuccessful()) {
                    Workout workout = new Workout();
                    Log.d("mReports", "workouts task success");
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        workout = doc.toObject(Workout.class);
                        sWorkouts.add(workout);
                        Log.d("mReports", "mWorkouts creation successful");
                    }
                } else {
                    Log.d("mReports", "failed to make mWorkouts");
                }
            }
        });
        return mWorkoutOptions;
    }

    public FirestoreRecyclerOptions<Message> setMessages() {
        // Build the database
        FirestoreRecyclerOptions<Message> mMessageOptions = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(messageQuery, Message.class)
                .build();

        messageQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Message message = new Message();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        message = doc.toObject(Message.class);
                        sMessages.add(message);
                    }
                } else {
                    Log.d("mReports", "failed to make mUsers");
                }
            }
        });
        return mMessageOptions;
    }
}
