package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.fragments.ReportListFragment;
import zachg.gsctrainingandnutritiontracker.fragments.WorkoutListFragment;
import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AdminClientProfileViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo = FirestoreRepository.getInstance();

    public String exerciseName, exerciseReps;

    public User user = new User();
    public User client = new User();
    public Workout workout = new Workout();
    public Exercise exercise = new Exercise();
    public Exercise exercise2 = new Exercise();
    public Exercise exercise3 = new Exercise();
    public Exercise exercise4 = new Exercise();
    public Exercise exercise5 = new Exercise();
    public ArrayList<Exercise> exerciseArray = new ArrayList<>();

    private ArrayList<EditText> exerciseNameEditTextArray = new ArrayList<>();
    private ArrayList<EditText> exerciseRepsEditTextArray = new ArrayList<>();
    private ArrayList<EditText> exerciseWeightEditTextArray = new ArrayList<>();

    private ObservableField<String> etGeneratedExerciseName = new ObservableField<String>();
    private ObservableField<String> etGeneratedExerciseReps = new ObservableField<String>();
    private ObservableField<String> etGeneratedExerciseWeight = new ObservableField<String>();
    private ObservableField<String> etWorkoutTitle = new ObservableField<>();
    private ObservableField<Integer> etWorkoutDay = new ObservableField<>();

    public MutableLiveData<String> generatedExerciseName = new MutableLiveData<String>();
    public MutableLiveData<String> generatedExerciseReps = new MutableLiveData<String>();
    public MutableLiveData<String> generatedExerciseWeight = new MutableLiveData<String>();
    public MutableLiveData<String> newExerciseName = new MutableLiveData<String>();
    public MutableLiveData<String> newExerciseReps = new MutableLiveData<String>();
    public MutableLiveData<String> newExerciseWeight = new MutableLiveData<String>();
    public MutableLiveData<String> newExerciseName2 = new MutableLiveData<String>();
    public MutableLiveData<String> newExerciseReps2 = new MutableLiveData<String>();
    public MutableLiveData<String> newExerciseWeight2 = new MutableLiveData<String>();
    public MutableLiveData<String> newExerciseName3 = new MutableLiveData<String>();
    public MutableLiveData<String> newExerciseReps3 = new MutableLiveData<String>();
    public MutableLiveData<String> newExerciseWeight3 = new MutableLiveData<String>();
    public MutableLiveData<String> newExerciseName4 = new MutableLiveData<String>();
    public MutableLiveData<String> newExerciseReps4 = new MutableLiveData<String>();
    public MutableLiveData<String> newExerciseWeight4 = new MutableLiveData<String>();
    public MutableLiveData<String> newExerciseName5 = new MutableLiveData<String>();
    public MutableLiveData<String> newExerciseReps5 = new MutableLiveData<String>();
    public MutableLiveData<String> newExerciseWeight5 = new MutableLiveData<String>();

    public MutableLiveData<String> workoutTitleLiveData = new MutableLiveData<String>();

    public MutableLiveData<String> onError = new MutableLiveData<>();

    public static String DUPLICATE_WORKOUT_TITLE = "Workout title already in use.";
    public static String WORKOUT_TITLE_NULL = "Workout title is null.";
    public static int ex1 = 0, ex2 = 1, ex3 = 2, ex4 = 3, ex5 = 4;   // numbers for exercise array elements

    public AdminClientProfileViewModel() {}

    public void init() {}

    public void getEtValues(Workout workout, int w, User client, String exerciseName, String exerciseName2,
                            String exerciseName3, String exerciseName4, String exerciseName5, String exerciseReps,
                            String exerciseReps2, String exerciseReps3, String exerciseReps4, String exerciseReps5,
                            String exerciseWeight, String exerciseWeight2, String exerciseWeight3, String exerciseWeight4,
                            String exerciseWeight5, LinearLayout ll) {

        Exercise exercise = new Exercise(exerciseName, exerciseReps, exerciseWeight);
        Exercise exercise2 = new Exercise(exerciseName2, exerciseReps2, exerciseWeight2);
        Exercise exercise3 = new Exercise(exerciseName3, exerciseReps3, exerciseWeight3);
        Exercise exercise4 = new Exercise(exerciseName4, exerciseReps4, exerciseWeight4);
        Exercise exercise5 = new Exercise(exerciseName5, exerciseReps5, exerciseWeight5);

        exerciseArray.add(ex1, exercise);
        exerciseArray.add(ex2, exercise2);
        exerciseArray.add(ex3, exercise3);
        exerciseArray.add(ex4, exercise4);
        exerciseArray.add(ex5, exercise5);

        // set exerciseName and exerciseReps
        for (int i=0; i < exerciseNameEditTextArray.size(); i++) {
            String exName = exerciseNameEditTextArray.get(i).getText().toString();
            String exReps = exerciseRepsEditTextArray.get(i).getText().toString();
            String exWeight = exerciseWeightEditTextArray.get(i).getText().toString();
            Exercise generatedExercise = new Exercise(exName, exReps, exWeight);
            exerciseArray.add(generatedExercise);
        }

        nullWorkoutTitleCheck(client, workout, exerciseArray);
    }

    // Checks if WorkoutTitle is null
    public void nullWorkoutTitleCheck(User client, Workout workout, ArrayList<Exercise> exerciseArray) {
        if (workout.getWorkoutTitle().length() == 0) {
            onError.setValue(WORKOUT_TITLE_NULL);
            return;
        } else {
            duplicateWorkoutTitleCheck(client, workout, exerciseArray);
        }
    }

    // Checks if workoutTitle is already used for this user
    public void duplicateWorkoutTitleCheck(User user, Workout workout, ArrayList<Exercise> exerciseArray) {
        this.user = user;
        this.exerciseArray = exerciseArray;
        repo.setQuerySnapshotOnCompleteListener(this);
        repo.duplicateWorkoutTitleCheck(user, workout);
    }

    // Write workouts to the repo
    public void writeToWorkouts(User user, Workout workout, ArrayList<Exercise> exerciseArray) {

        workout.setClientName(user.getClientName());
        workout.setEmail(user.getEmail());
        workout.setExercises(exerciseArray);

        repo.writeWorkoutsToRepo(user, workout);
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        QuerySnapshot qs = task.getResult();
        if (qs.size() > 0) {
            onError.setValue(DUPLICATE_WORKOUT_TITLE);
        } else {
            writeToWorkouts(user, workout, exerciseArray);
        }
    }
}