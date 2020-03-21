package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class WorkoutViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo = new FirestoreRepository();
    public MutableLiveData<Workout> workoutLiveData = new MutableLiveData<>();
    public MutableLiveData<FirestoreRecyclerOptions<ArrayList<Exercise>>> exerciseLiveData = new MutableLiveData<>();
    public MutableLiveData<String> workoutTitleLiveData = new MutableLiveData<String>();
    public MutableLiveData<String> onError = new MutableLiveData<>();
    public MutableLiveData<Boolean> workoutDeleted = new MutableLiveData<>();
    public MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    public ArrayList<Exercise> exerciseArray = new ArrayList<>();

    public Workout workout = new Workout();
    public Exercise exercise = new Exercise();
    public User client = new User();
    public String TAG = "WorkoutViewModel";
    public String dateString;
    public StringBuilder exerciseStringBuilder = new StringBuilder(5000);
    public String workoutTitle;
    public boolean newWorkout;

    public static String DUPLICATE_WORKOUT_TITLE = "Workout title already in use.";
    public static String WORKOUT_TITLE_NULL = "Workout title is null.";

    public WorkoutViewModel() {}

    public void init(User user, Workout workout) {
        repo = FirestoreRepository.getInstance();
        this.client = user;
        this.workout = workout;
        this.workoutTitle = workout.getWorkoutTitle();
        if (workout.getIsNew()) {
            addNewWorkout();
        }
    }

    // Create a workout with five initial exercises
    public void addNewWorkout() {
        int i = 0;
        while (i < 5) {
            exerciseArray.add(i, exercise);
            i++;
        }
        workout.setExercises(exerciseArray);
        workout.setWorkoutTitle("charlie");
        client.setEmail("p@p.com");
        repo.setQuerySnapshotOnCompleteListener(this);
        repo.createInitialExercises(client, workout);
        exerciseLiveData.setValue(repo.getExercisesFromRepo(client, workout));
    }

    public MutableLiveData<FirestoreRecyclerOptions<ArrayList<Exercise>>> getExerciseLiveData() {
        return exerciseLiveData;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }

    // Checks if WorkoutTitle is null
    public void nullWorkoutTitleCheck(User client, Workout workout) {
        if (workout.getWorkoutTitle().length() == 0) {
            onError.setValue(WORKOUT_TITLE_NULL);
            return;
        } else {
            duplicateWorkoutTitleCheck(client, workout);
        }
    }

    // Checks if workoutTitle is already used for this user
    public void duplicateWorkoutTitleCheck(User user, Workout workout) {
        this.client = user;
//        repo.setQuerySnapshotOnCompleteListener(this);
        repo.duplicateWorkoutTitleCheck(user, workout);
    }

    // Deletes Workout from the repository
    public void deleteWorkout(User client, Workout workout) {
        repo.deleteWorkout(client, workout);
        workoutDeleted.setValue(true);
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {

    }

//    @Override
//    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//        QuerySnapshot qs = task.getResult();
//        if (qs.size() > 0) {
//            onError.setValue(DUPLICATE_WORKOUT_TITLE);
//        } else {
//            repo.updateWorkout(client, workout);
//        }
//    }
}
