package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;
import zachg.gsctrainingandnutritiontracker.utils.WorkoutCallback;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class ViewReportViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo;
    private Report currentReport = new Report();
    private User user = new User();
    private User client = new User();
    private Workout workout = new Workout();
    private MutableLiveData<Report> reportLiveData = new MutableLiveData<>();
    private WorkoutCallback workoutCallback;

    public MutableLiveData<FirestoreRecyclerOptions<Exercise>> exerciseLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    public MutableLiveData<String> onError = new MutableLiveData<>();

    // init getting null data for user
    public void init(User user, User client, Report report) {
        final StringBuilder str = new StringBuilder();
        this.user = user;
        this.client = client;
        this.currentReport = report;
        this.workout = report.getWorkout();
        repo = FirestoreRepository.getInstance();
        repo.setQuerySnapshotOnCompleteListener(this);
        repo.getReportsFromRepo(client);
    }

    public MutableLiveData<FirestoreRecyclerOptions<Exercise>> getExerciseLiveData() {
        return exerciseLiveData;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot doc : task.getResult()) {
                Report report = doc.toObject(Report.class);
                currentReport = report;
            }
            repo.getExercisesFromRepo(user, workout);
        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
        }
    }
}