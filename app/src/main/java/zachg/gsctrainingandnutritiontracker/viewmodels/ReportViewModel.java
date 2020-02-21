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

public class ReportViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo = new FirestoreRepository();
    public MutableLiveData<Workout> workoutLiveData = new MutableLiveData<>();
    public MutableLiveData<FirestoreRecyclerOptions<Exercise>> exerciseLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public ObservableField<String> dailyWeight = new ObservableField<>();
    public ObservableField<String> comments = new ObservableField<>();
    public ObservableField<String> exerciseWeight = new ObservableField<>();

    public Report report = new Report();
    public Workout workout = new Workout();
    public Exercise exercise = new Exercise();
    public ArrayList<Exercise> exerciseArrayList = new ArrayList<>();
    public User currentUser = new User();
    public String TAG = "ReportViewModel";
    public String dateString;
    public StringBuilder exerciseStringBuilder = new StringBuilder(5000);
    public String workoutTitle;
    public int workoutDay;

    public ReportViewModel() {}

    public void init(User user, Report report) {
        repo = FirestoreRepository.getInstance();
        this.currentUser = user;
        this.report = report;
        this.dateString = report.getDateString();
        workoutDay = currentUser.getWorkoutDay();
        repo.setSnapshotOnCompleteListener(this);
        Log.d(TAG, "workoutday: " + workoutDay);
        Log.d(TAG, "user workoutday: " + currentUser.getWorkoutDay());
        repo.getExercisesForIteration(currentUser, workoutDay);
        exerciseLiveData.setValue(repo.getExercisesFromRepo(currentUser, workoutDay));
    }

    public MutableLiveData<FirestoreRecyclerOptions<Exercise>> getExercises() {
        return exerciseLiveData;
    }

    public void getExerciseListInfo() {
        // iterate thru exercises, copy name, weight, reps, write them to reports
        for (int i = 0; i < exerciseArrayList.size(); i++) {
            // set these into a single String and add it to fullreport;
            exerciseStringBuilder.append(exerciseArrayList.get(i).getExerciseName());
            exerciseStringBuilder.append(exerciseArrayList.get(i).getExerciseWeight());
            exerciseStringBuilder.append(exerciseArrayList.get(i).getReps());
        }
        report.setExerciseString(String.valueOf(exerciseStringBuilder));
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }

    // Writes report to the Repository
    public void writeReport(User currentUser, Report report) {
            Report generatedReport = new Report(report.getClientName(), currentUser.getEmail(),
                    dailyWeight.get(), exerciseWeight.get(), comments.get(), report.getDateString(),
                    report.getWorkoutTitle(), report.getExerciseString());
            repo.writeReportToRepo(generatedReport);

            iterateWorkoutNum(currentUser);
    }

    public void iterateWorkoutNum(User user) {
        if (user.getWorkoutDay()+1 < exerciseArrayList.size()) {
            user.setWorkoutDay(user.getWorkoutDay() + 1);
        } else {
            user.setWorkoutDay(1);
        }
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            Log.d(TAG, "size: " + String.valueOf(task.getResult().size()));
            for (QueryDocumentSnapshot doc : task.getResult()) {
                for (int i = 0; i <  task.getResult().size(); i++) {
                    Exercise exercise = doc.toObject(Exercise.class);
                    exerciseArrayList.add(i, exercise);
                    Log.d(TAG, "exercise " + i + " : " + exercise.getExerciseName());
                }
                getExerciseListInfo();
            }
        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
        }
    }
}