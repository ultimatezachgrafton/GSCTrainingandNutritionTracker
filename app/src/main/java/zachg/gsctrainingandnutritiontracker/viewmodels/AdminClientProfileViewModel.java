package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;
import android.widget.Toast;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AdminClientProfileViewModel extends ViewModel {

    private FirestoreRepository repo = FirestoreRepository.getInstance();

    public String exerciseName, exerciseReps, workoutTitle, workoutDay;
    public Exercise exercise = new Exercise();
    public Exercise exercise2 = new Exercise();
    public Exercise exercise3 = new Exercise();
    public Exercise exercise4 = new Exercise();
    public Exercise exercise5 = new Exercise();
    public Exercise exercise6 = new Exercise();
    public Exercise exercise7 = new Exercise();
    public List<Exercise> exerciseList = new ArrayList();

    private ObservableField<String> etExerciseName = new ObservableField<>();
    private ObservableField<String> etExerciseName2 = new ObservableField<>();
    private ObservableField<String> etExerciseName3 = new ObservableField<>();
    private ObservableField<String> etExerciseName4 = new ObservableField<>();
    private ObservableField<String> etExerciseName5 = new ObservableField<>();
    private ObservableField<String> etExerciseName6 = new ObservableField<>();
    private ObservableField<String> etExerciseName7 = new ObservableField<>();
    private ObservableField<String> etWorkoutTitle = new ObservableField<>();
    private ObservableField<String> etWorkoutDay = new ObservableField<>();
    private ObservableField<String> etExerciseReps = new ObservableField<>();
    private ObservableField<String> etExerciseReps2 = new ObservableField<>();
    private ObservableField<String> etExerciseReps3 = new ObservableField<>();
    private ObservableField<String> etExerciseReps4 = new ObservableField<>();
    private ObservableField<String> etExerciseReps5 = new ObservableField<>();
    private ObservableField<String> etExerciseReps6 = new ObservableField<>();
    private ObservableField<String> etExerciseReps7 = new ObservableField<>();
    public MutableLiveData<Exercise> newExercise = new MutableLiveData<Exercise>();
    public MutableLiveData<Exercise> newExercise2 = new MutableLiveData<Exercise>();
    public MutableLiveData<Exercise> newExercise3 = new MutableLiveData<Exercise>();
    public MutableLiveData<Exercise> newExercise4 = new MutableLiveData<Exercise>();
    public MutableLiveData<Exercise> newExercise5 = new MutableLiveData<Exercise>();
    public MutableLiveData<Exercise> newExercise6 = new MutableLiveData<Exercise>();
    public MutableLiveData<Exercise> newExercise7 = new MutableLiveData<Exercise>();
    public MutableLiveData<Workout> newWorkout = new MutableLiveData<Workout>();

    public AdminClientProfileViewModel() {}

    public void init() {
    }

    public void writeToWorkouts(User user, Exercise exercise, Exercise exercise2, Exercise exercise3,
                                Exercise exercise4, Exercise exercise5, Exercise exercise6,
                                Exercise exercise7, String workoutDay, String workoutTitle) {
        this.exercise = exercise;
        this.exercise2 = exercise2;
        this.exercise3 = exercise3;
        this.exercise4 = exercise4;
        this.exercise5 = exercise5;
        this.exercise6 = exercise6;
        this.exercise7 = exercise7;

        this.workoutDay = workoutDay;
        this.workoutTitle = workoutTitle;

        exerciseList.add(0, exercise);
        exerciseList.add(1, exercise2);
        exerciseList.add(2, exercise3);
        exerciseList.add(3, exercise4);
        exerciseList.add(4, exercise5);
        exerciseList.add(5, exercise6);
        exerciseList.add(6, exercise7);

        Workout workout = new Workout(user.getClientName(), user.getEmail(), workoutTitle);
        workout.setExerciseList(exerciseList);
        workout.setWorkoutTitle(workoutTitle);

        // if workoutTitle is null, set null

        repo.writeWorkoutsToRepo(user, workout);
    }
}