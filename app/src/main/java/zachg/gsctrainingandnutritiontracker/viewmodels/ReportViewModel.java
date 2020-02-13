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

public class ReportViewModel extends ViewModel {

    private FirestoreRepository repo = new FirestoreRepository();
    public MutableLiveData<FirestoreRecyclerOptions<Workout>> workoutLiveData = new MutableLiveData<>();
    public MutableLiveData<FirestoreRecyclerOptions<Exercise>> exerciseLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public ObservableField<String> dailyWeight = new ObservableField<>("");
    public ObservableField<String> comments = new ObservableField<>("");
    public ObservableField<String> exerciseWeight = new ObservableField<>("");

    public Report report = new Report();
    public Workout workout = new Workout();
    public Exercise exercise = new Exercise();
    private ArrayList<Exercise> exercises = new ArrayList<Exercise>();
    public User currentUser = new User();
    public String TAG = "ReportViewModel";
    public String dateString;

    public ReportViewModel() {}

    public void init(User user, Report report) {
        repo = FirestoreRepository.getInstance();
        this.currentUser = user;
        this.report = report;
        this.dateString = report.getDateString();
        exerciseLiveData.setValue(repo.getExercisesFromRepo(currentUser, 0));
        // exercise
//        exerciseLiveData.setValue(exerciseArrayList);
    }

    public MutableLiveData<FirestoreRecyclerOptions<Workout>> getWorkouts() {
        return workoutLiveData;
    }

    public MutableLiveData<FirestoreRecyclerOptions<Exercise>> getExercises() {
        return exerciseLiveData;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }

    // Writes report to the Repository
    public void writeReport(User currentUser, Report report) {
        Report generatedReport = new Report(report.getClientName(), currentUser.getEmail(),
                dailyWeight.get(), exerciseWeight.get(), comments.get(), report.getDateString());
        // TODO: get workout to access title and day
        Log.d(TAG, generatedReport.getClientName() + generatedReport.getDateString());
        repo.writeReportToRepo(generatedReport);

        // TODO: write iterated workoutNum to user's fstore data
        //iterateWorkoutNum(currentUser);
    }

    public void iterateWorkoutNum(User user) {
        user.setCurrentWorkoutNum(user.getCurrentWorkoutNum()+1);
    }

//    @Override
//    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//        if (task.isSuccessful()) {
//            for (QueryDocumentSnapshot doc : task.getResult()) {
//                if (doc.exists()) {
//                    exercise = doc.toObject(Exercise.class);
//
//                    exerciseLiveData.setValue(exercise); // get this current workout's exercises
//
//                    exercises = workout.getExercises();
//                }
//            }
//            Log.d(TAG, "list size: " + exercises.size());
//            Log.d(TAG, "list: " + workout.getExercises() + workout.getWorkoutTitle());
////            Log.d(TAG, "element 0: " + String.valueOf(exercises.get(0).getExerciseName()));
//        } else {
//            Log.d(TAG, "Error getting documents: ", task.getException());
//            return;
//        }
//    }
}