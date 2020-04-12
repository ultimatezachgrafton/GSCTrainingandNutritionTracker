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

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.SingleLiveEvent;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class ClientReportViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo = new FirestoreRepository();
    private SingleLiveEvent<Workout> workoutLiveData = new SingleLiveEvent<>();
    private SingleLiveEvent<FirestoreRecyclerOptions<Exercise>> exerciseLiveData = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> isUpdating = new SingleLiveEvent<>();
    private SingleLiveEvent<String> onError = new SingleLiveEvent<>();
    private SingleLiveEvent<String> onSuccess = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> areExcercisesCollected = new SingleLiveEvent<>();
    public SingleLiveEvent<ArrayList<Exercise>> exerciseArrayListLiveData = new SingleLiveEvent<>();

    public ArrayList<Exercise> exerciseArrayList = new ArrayList<>();

    public Report report = new Report();
    public Workout workout = new Workout();
    public Exercise exercise = new Exercise();
    private User currentUser = new User();
    public String TAG = "ReportViewModel";
    public String REPORT_SUCCESS = "Report successfully written.";
    public String dateString;
    public String workoutTitle;
    private boolean areExercisesCollectedBool = false;

    public ClientReportViewModel() {}

    public void init(User user, Report report, Workout workout) {
        this.currentUser = user;
        this.report = report;
        this.dateString = report.getDateString();
        this.report.setWorkoutTitle(workout.getWorkoutTitle());
        this.report.setEmail(user.getEmail());
        this.workout = workout;
        this.report.setWorkout(workout);
        createExerciseArrayList();
    }

    public void createExerciseArrayList() {
        exerciseLiveData.setValue(repo.getExercisesFromRepo(currentUser, report.getWorkout()));
    }

    public SingleLiveEvent<FirestoreRecyclerOptions<Exercise>> getExerciseLiveData() {
        return exerciseLiveData;
    }

    public SingleLiveEvent<Boolean> getIsUpdating() {
        return isUpdating;
    }

    public void getExercisesForArray() {
        repo.setQuerySnapshotOnCompleteListener(this);
        repo.getExercisesForArray(currentUser, report.getWorkout());
    }

    // Writes report to the Repository
    public void writeReportToRepo() {
            // TODO create these inside Report constructor or something
            String dailyWeight = report.getDailyWeight();
            report.setDailyWeightString(dailyWeight);
            String dailyWeightString = report.getDailyWeightString();

            Report generatedReport = new Report(report.getClientName(), currentUser.getEmail(),
                    dailyWeight, dailyWeightString, report.getComments(), this.dateString,
                    report.getWorkoutTitle(), exerciseArrayList);
            repo.writeReportToRepo(currentUser, generatedReport);
            onSuccess.setValue(REPORT_SUCCESS);
    }

    public SingleLiveEvent<String> getOnSuccess() {
        return onSuccess;
    }

    public SingleLiveEvent<String> getOnError() {
        return onError;
    }

    public SingleLiveEvent<Boolean> getAreExcercisesCollected() {
        return areExcercisesCollected;
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot doc : task.getResult()) {
                Exercise exercise = doc.toObject(Exercise.class);
                exerciseArrayList.add(exercise);
            }
            writeReportToRepo();
        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
        }
    }
}