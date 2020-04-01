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
import java.util.UUID;

import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.SingleLiveEvent;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class AdminUpdateWorkoutViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo = new FirestoreRepository();
    private SingleLiveEvent<Workout> workoutLiveData = new SingleLiveEvent<>();
    private SingleLiveEvent<FirestoreRecyclerOptions<Exercise>> exerciseLiveData = new SingleLiveEvent<>();
    private SingleLiveEvent<String> workoutTitleLiveData = new SingleLiveEvent<String>();
    private SingleLiveEvent<String> onError = new SingleLiveEvent<>();
    private SingleLiveEvent<String> onSuccess = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> workoutDeleted = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> isUpdating = new SingleLiveEvent<>();
    public ArrayList<Exercise> exerciseArray = new ArrayList<>();

    public Workout workout = new Workout();
    public Exercise exercise = new Exercise();
    public User client = new User();
    public String TAG = "AdminUpdateWorkoutViewModel";
    public String dateString;
    public String workoutTitle;

    public static String DUPLICATE_WORKOUT_TITLE = "Workout title already in use.";
    public static String WORKOUT_TITLE_NULL = "Workout title is null.";
    public static String WORKOUT_UPDATED = "Workout updated.";
    public static String WORKOUT_DELETED = "Workout deleted";

    public AdminUpdateWorkoutViewModel() {}

    public void init(User user, Workout workout) {
        repo = FirestoreRepository.getInstance();
        this.client = user;
        this.workout = workout;
        this.workoutTitle = workout.getWorkoutTitle();
        if (workout.getIsNew()) {
            createInitialExercises();
        }
    }

    // TODO test
    public void addOne() {
        addBlankExercises(1);
    }

    public void addThree() { addBlankExercises(3); }

    public void addFive() { addBlankExercises(5); }

    public void createInitialExercises() { addBlankExercises(5); }

    // Create a workout with five initial exercises
    public void addBlankExercises(int i) {
        ArrayList<Exercise> exerciseArray = new ArrayList<>();
        int j = 0;
        while (j < i) {
            exercise.setId(UUID.randomUUID().toString());
            exerciseArray.add(exercise);
            repo.createBlankExercises(client, workout, exercise);
            j++;
        }
        workout.setExercises(exerciseArray);
        exerciseLiveData.setValue(repo.getExercisesFromRepo(client, workout));
    }

    public SingleLiveEvent<FirestoreRecyclerOptions<Exercise>> getExerciseLiveData() {
        return exerciseLiveData;
    }

    public SingleLiveEvent<Boolean> getIsUpdating() {
        return isUpdating;
    }

    public SingleLiveEvent<String> getWorkoutTitleLiveData() { return workoutTitleLiveData; }

    public MutableLiveData<String> getOnError() { return onError; }

    public SingleLiveEvent<Boolean> getWorkoutDeleted() { return workoutDeleted; }

    public SingleLiveEvent<String> getOnSuccess() { return onSuccess; }

    // Checks if WorkoutTitle is null
    public void nullWorkoutTitleCheck(User client, Workout workout) {
        if (workout.getWorkoutTitle() == null) {
            onError.setValue(WORKOUT_TITLE_NULL);
            return;
        } else {
            duplicateWorkoutTitleCheck(client, workout);
        }
    }

    // Checks if workoutTitle is already used for this user
    public void duplicateWorkoutTitleCheck(User client, Workout workout) {
        this.client = client;
        repo.setQuerySnapshotOnCompleteListener(this);
        repo.duplicateWorkoutTitleCheck(client, workout.getWorkoutTitle());
    }

    // Deletes Workout from the repository
    public void deleteWorkout(User client, Workout workout) {
        repo.deleteWorkout(client, workout);
        onSuccess.setValue(WORKOUT_DELETED);
        workoutDeleted.setValue(true);
    }

    // Callback to  update and override previous workout information
    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        QuerySnapshot qs = task.getResult();
        if (qs.size() > 0) {
            onError.setValue(DUPLICATE_WORKOUT_TITLE);
        } else {
            onSuccess.setValue(WORKOUT_UPDATED);
            repo.updateWorkout(client, workout);
        }
    }
}
