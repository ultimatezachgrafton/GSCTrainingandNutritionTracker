package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ClientReportViewModel extends ViewModel {

    private FirestoreRepository repo = new FirestoreRepository();
    public MutableLiveData<Workout> workoutLiveData = new MutableLiveData<>();
    public MutableLiveData<FirestoreRecyclerOptions<Exercise>> exerciseLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    public MutableLiveData<String> onError = new MutableLiveData<>();

    public ObservableField<String> dailyWeight = new ObservableField<>();
    public ObservableField<String> comments = new ObservableField<>();
    public ObservableField<String> exerciseWeight = new ObservableField<>();

    public Report report = new Report();
    public Workout workout = new Workout();
    public Exercise exercise = new Exercise();
    public User currentUser = new User();
    public String TAG = "ReportViewModel";
    public String dateString;
    public String workoutTitle;

    public ClientReportViewModel() {}

    public void init(User user, Report report, Workout workout) {
        repo = FirestoreRepository.getInstance();
        this.currentUser = user;
        this.report = report;
        this.dateString = report.getDateString();
        getExercisesFromRepo(user, workout);
    }

    public MutableLiveData<FirestoreRecyclerOptions<Exercise>> getExerciseLiveData() {
        return exerciseLiveData;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }

    public void getExercisesFromRepo(User user, Workout workout) {
        workout.setWorkoutTitle("charlie");
        repo.getExercisesFromRepo(user, workout);
    }

    // TODO: not working, test after rv installed
    // Writes report to the Repository
    public void writeReport(User currentUser, Report report) {
            Report generatedReport = new Report(report.getClientName(), currentUser.getEmail(),
                    dailyWeight.get(), exerciseWeight.get(), comments.get(), report.getDateString(),
                    report.getWorkoutTitle(), report.getExerciseString());
            generatedReport.setDateString("doink");
            repo.writeReportToRepo(generatedReport);
    }
}