package zachg.gsctrainingandnutritiontracker.viewmodels;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class AdminClientProfileViewModel extends ViewModel {

    private FirestoreRepository repo = FirestoreRepository.getInstance();

    public String exerciseName, exerciseReps, workoutTitle, workoutDay;
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

    private ObservableField<String> etGeneratedExerciseName = new ObservableField<String>();
    private ObservableField<String> etGeneratedExerciseReps = new ObservableField<String>();

    private ObservableField<String> etWorkoutTitle = new ObservableField<>();
    private ObservableField<String> etWorkoutDay = new ObservableField<>();

    public MutableLiveData<String> generatedExerciseName = new MutableLiveData<String>();
    public MutableLiveData<String> generatedExerciseReps = new MutableLiveData<String>();

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

    public MutableLiveData<String> workoutTitleLiveData = new MutableLiveData<String>();
    public MutableLiveData<String> workoutDayLiveData = new MutableLiveData<String>();


    public AdminClientProfileViewModel() {}

    public void init() {
    }

    public void writeToWorkouts(User user, Exercise exercise, Exercise exercise2, Exercise exercise3,
                                Exercise exercise4, Exercise exercise5, String workoutDay, String workoutTitle) {
        this.exercise = exercise;
        this.exercise2 = exercise2;
        this.exercise3 = exercise3;
        this.exercise4 = exercise4;
        this.exercise5 = exercise5;

//        Log.d(TAG, exercise5.getExerciseName());

        this.workoutDay = workoutDay;
        this.workoutTitle = workoutTitle;

        exercises.add(0, exercise);
        exercises.add(1, exercise2);
        exercises.add(2, exercise3);
        exercises.add(3, exercise4);
        exercises.add(4, exercise5);

        Workout workout = new Workout(user.getClientName(), user.getEmail(), workoutTitle);
        workout.setExercises(exercises);
        workout.setWorkoutTitle(workoutTitle);
        workout.setWorkoutDay(workoutDay);

        // if workoutTitle is null, set null

        repo.writeWorkoutsToRepo(user, workout);
    }
}