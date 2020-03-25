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
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class AdminUpdateWorkoutViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo = new FirestoreRepository();
    public MutableLiveData<Workout> workoutLiveData = new MutableLiveData<>();
    public MutableLiveData<FirestoreRecyclerOptions<Exercise>> exerciseLiveData = new MutableLiveData<>();
    public MutableLiveData<String> workoutTitleLiveData = new MutableLiveData<String>();
    public MutableLiveData<String> onError = new MutableLiveData<>();
    public MutableLiveData<Boolean> workoutDeleted = new MutableLiveData<>();
    public MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    public ArrayList<Exercise> exerciseArray = new ArrayList<>();

    public Workout workout = new Workout();
    public Exercise exercise = new Exercise();
    public User client = new User();
    public String TAG = "AdminUpdateWorkoutViewModel";
    public String dateString;
    public String workoutTitle;

    public static String DUPLICATE_WORKOUT_TITLE = "Workout title already in use.";
    public static String WORKOUT_TITLE_NULL = "Workout title is null.";

    public AdminUpdateWorkoutViewModel() {}

    public void init(User user, Workout workout) {
        repo = FirestoreRepository.getInstance();
        this.client = user;
        this.workout = workout;
        this.workoutTitle = workout.getWorkoutTitle();
        if (workout.getIsNew()) {
            addFive();
        }
    }

    // TODO test
    public void addOne() {
        addBlankExercises(1);
    }

    public void addThree() {
        addBlankExercises(3);
    }

    public void addFive() {
        addBlankExercises(5);
    }

    // Create a workout with five initial exercises
    public void addBlankExercises(int i) {
        while (i < 5) {
            exercise.setId(UUID.randomUUID().toString());
            exerciseArray.add(i, exercise);
            repo.createBlankExercises(client, workout, exercise);
            i++;
        }
        workout.setExercises(exerciseArray);
        exerciseLiveData.setValue(repo.getExercisesFromRepo(client, workout));
    }

    public MutableLiveData<FirestoreRecyclerOptions<Exercise>> getExerciseLiveData() {
        return exerciseLiveData;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }

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
        workoutDeleted.setValue(true);
    }

    // Callback to  update and override previous workout information
    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        QuerySnapshot qs = task.getResult();
        if (qs.size() > 0) {
            onError.setValue(DUPLICATE_WORKOUT_TITLE);
        } else {
            repo.updateWorkout(client, workout);
        }
    }
}
