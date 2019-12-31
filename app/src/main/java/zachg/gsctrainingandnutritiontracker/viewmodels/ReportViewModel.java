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

    public Report report = new Report();
    public User currentUser = new User();
    public String TAG = "ReportViewModel";
    public String dateString;

    public ReportViewModel() {}

    public void init(User user, Report report) {
        repo = FirestoreRepository.getInstance();
        this.currentUser = user;
        this.report = report;
        this.dateString = report.getDateString();
        workouts.setValue(repo.getWorkoutsFromRepo(currentUser));
    }

    public MutableLiveData<FirestoreRecyclerOptions<Workout>> getWorkouts() {
        return workouts;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }

    // Writes report to the Repository
    public void writeReport(User currentUser, Report report) {
        Report generatedReport = new Report(report.getClientName(), currentUser.getEmail(), dailyWeight.get(), exerciseWeight.get(), comments.get(), report.getDateString());
        Log.d(TAG, generatedReport.getClientName() + generatedReport.getDateString());
        repo.writeReportToRepo(generatedReport);

        // TODO: write iterated workoutNum to user's fstore data
        //iterateWorkoutNum(currentUser);
    }

    public void iterateWorkoutNum(User user) {
        user.setCurrentWorkoutNum(user.getCurrentWorkoutNum()+1);
    }

}