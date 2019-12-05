package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class ReportViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo;
    public ObservableField<String> dailyWeight = new ObservableField<>();
    public ObservableField<String> comments = new ObservableField<>();
    public MutableLiveData<FirestoreRecyclerOptions<Workout>> workouts = new MutableLiveData<>();
    public MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    public Report currentReport = new Report();
    public User currentUser = new User();
    public String TAG = "ReportViewModel";

    public ReportViewModel() {}

    // init getting null data for user
    public void init(User user, Report report) {
        repo = FirestoreRepository.getInstance();
        this.currentUser = user;
        this.currentReport = report;
        repo.setSnapshotOnCompleteListener(this);
        workouts.setValue(repo.getWorkoutsFromRepo(user));
    }

    public MutableLiveData<FirestoreRecyclerOptions<Workout>> getWorkouts() {
        return workouts;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }

    // Writes report to the Repository
    public void writeReport(Report report) {
        Log.d(TAG, "write Report");
        dailyWeight.set(report.getDailyWeight());

        comments.set(report.getComments());
        Report generatedReport = new Report(report.getClientName(), report.getDate(), String.valueOf(dailyWeight.get()), String.valueOf(comments.get()));

        repo.db.collection("users").document(report.getClientName()).collection("reports")
                .document(String.valueOf(report.getDate()))
                .set(generatedReport)
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
        // TODO: write iterated workoutNum to user's fstore data
    }

    // TODO: iterateWorkouts after saving workout;

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot doc : task.getResult()) {
                Workout workout = doc.toObject(Workout.class);
                //workouts.setValue(workout);
            }
        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
            return;
        }
    }
}