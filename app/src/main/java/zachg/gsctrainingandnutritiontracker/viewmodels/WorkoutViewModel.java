package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
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
    private MutableLiveData<ArrayList<Exercise>> exercises = new MutableLiveData<>();

    public MutableLiveData<String> generatedExerciseName = new MutableLiveData<String>();
    public MutableLiveData<String> generatedExerciseReps = new MutableLiveData<String>();
    public MutableLiveData<String> generatedExerciseWeight = new MutableLiveData<String>();
    public MutableLiveData<String> workoutTitleLiveData = new MutableLiveData<String>();
    public MutableLiveData<String> onError = new MutableLiveData<>();
    public MutableLiveData<Boolean> workoutDeleted = new MutableLiveData<>();

    public ObservableField<String> etGeneratedExerciseName = new ObservableField<String>();
    public ObservableField<String> etGeneratedExerciseReps = new ObservableField<String>();
    public ObservableField<String> etGeneratedExerciseWeight = new ObservableField<String>();
    public ObservableField<String> etWorkoutTitle = new ObservableField<>();

    public Workout workout = new Workout();
    public Exercise exercise = new Exercise();
    public ArrayList<Exercise> exerciseArrayList = new ArrayList<>();
    public User client = new User();
    public String TAG = "WorkoutViewModel";
    public String dateString;
    public StringBuilder exerciseStringBuilder = new StringBuilder(5000);
    public String workoutTitle;

    public static String DUPLICATE_WORKOUT_TITLE = "Workout title already in use.";
    public static String WORKOUT_TITLE_NULL = "Workout title is null.";

    public Exercise exercise2 = new Exercise();
    public Exercise exercise3 = new Exercise();
    public Exercise exercise4 = new Exercise();
    public Exercise exercise5 = new Exercise();

    public WorkoutViewModel() {}

    public void init(User user, Workout workout) {
        repo = FirestoreRepository.getInstance();
        this.client = user;
        this.workout = workout;
        this.workoutTitle = workout.getWorkoutTitle();
    }


    // Takes EditText values and passes them to ViewModel
    public void getEtValues(User client, Workout workout, ArrayList<EditText> exerciseNameEditTextArray, ArrayList<EditText>
            exerciseRepsEditTextArray, ArrayList<EditText> exerciseWeightEditTextArray) {
        ArrayList<Exercise> exArray = new ArrayList<Exercise>();

        // set exerciseName and exerciseReps
        for (int i=0; i < exerciseNameEditTextArray.size(); i++) {
            String exName = exerciseNameEditTextArray.get(i).getText().toString();
            String exReps = exerciseRepsEditTextArray.get(i).getText().toString();
            String exWeight = exerciseWeightEditTextArray.get(i).getText().toString();
            Exercise generatedExercise = new Exercise(exName, exReps, exWeight);
            exArray.add(generatedExercise);
        }

        workout.setExercises(exArray);
        nullWorkoutTitleCheck(client, workout);
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
        repo.setQuerySnapshotOnCompleteListener(this);
        repo.duplicateWorkoutTitleCheck(user, workout);
    }

    // Deletes Workout from the repository
    public void deleteWorkout(User client, Workout workout) {
        repo.deleteWorkout(client, workout);
        workoutDeleted.setValue(true);
    }

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
