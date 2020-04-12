package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.SingleLiveEvent;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class ViewReportViewModel extends ViewModel {

    private FirestoreRepository repo;
    private Report currentReport = new Report();
    private User user = new User();
    private User client = new User();
    private Workout workout = new Workout();

    private SingleLiveEvent<FirestoreRecyclerOptions<Exercise>> exerciseLiveData = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> isUpdating = new SingleLiveEvent<>();
    private SingleLiveEvent<String> onError = new SingleLiveEvent<>();

    // init getting null data for user
    public void init(User user, User client, Report report) {
        this.user = user;
        this.client = client;
        this.currentReport = report;
        this.workout = report.getWorkout();
        exerciseLiveData.setValue(repo.getExercisesFromRepo(client, currentReport));
    }

    public SingleLiveEvent<FirestoreRecyclerOptions<Exercise>> getExerciseLiveData() {
        return exerciseLiveData;
    }

    public SingleLiveEvent<Boolean> getIsUpdating() {
        return isUpdating;
    }
}