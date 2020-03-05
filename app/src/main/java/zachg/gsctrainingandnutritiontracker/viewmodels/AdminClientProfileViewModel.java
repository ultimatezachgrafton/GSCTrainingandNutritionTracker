package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AdminClientProfileViewModel extends ViewModel {

    private FirestoreRepository repo = FirestoreRepository.getInstance();

    public String exerciseName, exerciseReps, workoutTitle;
//    public int workoutDay = 0;
    public Exercise exercise = new Exercise();
    public Exercise exercise2 = new Exercise();
    public Exercise exercise3 = new Exercise();
    public Exercise exercise4 = new Exercise();
    public Exercise exercise5 = new Exercise();
    public ArrayList<Exercise> exercises = new ArrayList();

    private ObservableField<String> etExerciseName = new ObservableField<>();
    private ObservableField<String> etExerciseName2 = new ObservableField<>();
    private ObservableField<String> etExerciseName3 = new ObservableField<>();
    private ObservableField<String> etExerciseName4 = new ObservableField<>();
    private ObservableField<String> etExerciseName5 = new ObservableField<>();

    private ObservableField<String> etExerciseReps = new ObservableField<>();
    private ObservableField<String> etExerciseReps2 = new ObservableField<>();
    private ObservableField<String> etExerciseReps3 = new ObservableField<>();
    private ObservableField<String> etExerciseReps4 = new ObservableField<>();
    private ObservableField<String> etExerciseReps5 = new ObservableField<>();

    private ObservableField<String> etExerciseWeight = new ObservableField<>();
    private ObservableField<String> etExerciseWeight2 = new ObservableField<>();
    private ObservableField<String> etExerciseWeight3 = new ObservableField<>();
    private ObservableField<String> etExerciseWeight4 = new ObservableField<>();
    private ObservableField<String> etExerciseWeight5 = new ObservableField<>();

    private ObservableField<String> etGeneratedExerciseName = new ObservableField<String>();
    private ObservableField<String> etGeneratedExerciseReps = new ObservableField<String>();
    private ObservableField<String> etGeneratedExerciseWeight = new ObservableField<String>();

    private ObservableField<String> etWorkoutTitle = new ObservableField<>();
//    private ObservableField<Integer> etWorkoutDay = new ObservableField<>();

    public MutableLiveData<String> generatedExerciseName = new MutableLiveData<String>();
    public MutableLiveData<String> generatedExerciseReps = new MutableLiveData<String>();
    public MutableLiveData<String> generatedExerciseWeight = new MutableLiveData<String>();

    public MutableLiveData<String> newExerciseName = new MutableLiveData<>();
    public MutableLiveData<String> newExerciseName2 = new MutableLiveData<>();
    public MutableLiveData<String> newExerciseName3 = new MutableLiveData<>();
    public MutableLiveData<String> newExerciseName4 = new MutableLiveData<>();
    public MutableLiveData<String> newExerciseName5 = new MutableLiveData<>();

    public MutableLiveData<String> newExerciseReps = new MutableLiveData<>();
    public MutableLiveData<String> newExerciseReps2 = new MutableLiveData<>();
    public MutableLiveData<String> newExerciseReps3 = new MutableLiveData<>();
    public MutableLiveData<String> newExerciseReps4 = new MutableLiveData<>();
    public MutableLiveData<String> newExerciseReps5 = new MutableLiveData<>();

    public MutableLiveData<String> newExerciseWeight = new MutableLiveData<>();
    public MutableLiveData<String> newExerciseWeight2 = new MutableLiveData<>();
    public MutableLiveData<String> newExerciseWeight3 = new MutableLiveData<>();
    public MutableLiveData<String> newExerciseWeight4 = new MutableLiveData<>();
    public MutableLiveData<String> newExerciseWeight5 = new MutableLiveData<>();

    public MutableLiveData<String> workoutTitleLiveData = new MutableLiveData<String>();
//    public MutableLiveData<Integer> workoutDayLiveData = new MutableLiveData<>();


    public AdminClientProfileViewModel() {}

    public void init() {}

    public void writeToWorkouts(User user, ArrayList<Exercise> exArray, int w, String workoutTitle) {

        for (int i = 0; i < exArray.size(); i++) {
            exercises.add(i, exArray.get(i));
        }

        Log.d(TAG, exercises.get(5).getExerciseName());

//        this.workoutDay = workoutDay;
        this.workoutTitle = workoutTitle;

        Workout workout = new Workout(user.getClientName(), user.getEmail(), workoutTitle);
        workout.setExercises(exercises);
        workout.setWorkoutTitle(workoutTitle);
//        workout.setWorkoutDay(3);

        // TODO: if workoutTitle is null, set null

        repo.writeWorkoutsToRepo(user, workout);
    }
}