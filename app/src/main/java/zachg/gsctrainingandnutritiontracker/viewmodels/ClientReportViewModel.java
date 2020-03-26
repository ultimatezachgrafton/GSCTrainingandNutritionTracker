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

public class ClientReportViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo = new FirestoreRepository();
    public MutableLiveData<Workout> workoutLiveData = new MutableLiveData<>();
    public MutableLiveData<FirestoreRecyclerOptions<Exercise>> exerciseLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    public MutableLiveData<String> onError = new MutableLiveData<>();
    public MutableLiveData<Boolean> areExercisesCollected = new MutableLiveData<>();

    public ArrayList<Exercise> exerciseArrayList = new ArrayList<>();

    public ObservableField<String> dailyWeight = new ObservableField<>();
    public ObservableField<String> comments = new ObservableField<>();

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
        this.report.setWorkoutTitle(workout.getWorkoutTitle());
        this.report.setEmail(user.getEmail());
        exerciseLiveData.setValue(repo.getExercisesFromRepo(user, workout));
    }

    public MutableLiveData<FirestoreRecyclerOptions<Exercise>> getExerciseLiveData() {
        return exerciseLiveData;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }

    // Writes report to the Repository
    public void writeReportToRepo(Report report) {

        // TODO create these inside Report constructor
        String dailyWeight = report.getDailyWeight();
        report.setDailyWeightString(dailyWeight);
        String dailyWeightString = report.getDailyWeightString();

        Report generatedReport = new Report(report.getClientName(), currentUser.getEmail(),
                dailyWeight, dailyWeightString, report.getComments(), report.getDateString(),
                report.getWorkoutTitle(), report.getExerciseArrayList());
        repo.writeReportToRepo(generatedReport);

    }

    // Collects Exercise data for Report
    public void collectExercises(User user, Report report) {
        Log.d(TAG, "in vm");
        repo.setQuerySnapshotOnCompleteListener(this);
        repo.getExercisesForReport(user, report);
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot doc : task.getResult()) {
                Exercise e = doc.toObject(Exercise.class);
                exerciseArrayList.add(e);
                Log.d(TAG, "in loop");
            } Log.d(TAG, "in oncomplete");
            report.setExerciseArrayList(exerciseArrayList);
            writeReportToRepo(report);
        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
        }
    }
}