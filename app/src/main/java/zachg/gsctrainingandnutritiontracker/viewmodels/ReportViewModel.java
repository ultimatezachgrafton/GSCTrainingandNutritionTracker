package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class ReportViewModel extends ViewModel {

    private FirestoreRepository repo = new FirestoreRepository();
    public MutableLiveData<FirestoreRecyclerOptions<Workout>> workouts = new MutableLiveData<>();
    public MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public ObservableField<String> dailyWeight = new ObservableField<>("");
    public ObservableField<String> comments = new ObservableField<>("");
    public ObservableField<String> exerciseWeight = new ObservableField<>("");

    public Report currentReport = new Report();
    public User currentUser = new User();
    public String TAG = "ReportViewModel";

    public ReportViewModel() {}

    public void init(User user) {
        repo = FirestoreRepository.getInstance();
        this.currentUser = user;
        workouts.setValue(repo.getWorkoutsFromRepo(currentUser));
    }

    public MutableLiveData<FirestoreRecyclerOptions<Workout>> getWorkouts() {
        return workouts;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }

    // Writes report to the Repository
    public void writeReport(User currentUser) {
        Report generatedReport = new Report(currentUser.getClientName(), dailyWeight.get(), exerciseWeight.get(), comments.get());
        repo.writeReportToRepo(generatedReport);
        // TODO: write iterated workoutNum to user's fstore data
        // TODO: iterateWorkouts after saving workout;
    }

}