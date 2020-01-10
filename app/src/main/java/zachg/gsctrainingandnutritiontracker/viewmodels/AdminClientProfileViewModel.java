package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AdminClientProfileViewModel extends ViewModel {

    private FirestoreRepository repo;
    private User currentUser = new User();

    public String exerciseName, exerciseReps;

    private ObservableField<String> etExerciseName = new ObservableField<>();
    private ObservableField<String> etWorkoutTitle = new ObservableField<>();
    private ObservableField<String> etExerciseReps = new ObservableField<>();
    private ObservableField<String> etDay = new ObservableField<>();
    public MutableLiveData<Exercise> newExercise = new MutableLiveData<Exercise>();

    public AdminClientProfileViewModel() {}

    public AdminClientProfileViewModel(User user) {
        this.currentUser = user;
        this.currentUser.setClientName(user.getFirstName(), user.getLastName());
    }

    public void init() {
        repo = FirestoreRepository.getInstance();
    }

    // Workout is array of exercises
    public void writeToWorkouts() {
        Workout workout = new Workout();
        Exercise exercise = new Exercise();

        // not saving workouts to db
        Log.d(TAG, "w2workouts");

        // exercise gets name
        setExerciseValues(exerciseName, exerciseReps);
        // sets user values
        workout.setClientName("hal");
        repo.writeWorkoutsToRepo(currentUser, workout);

        // TODO: write exercise to workout array where the value == exerciseNum
//        for (int i = 0; i < workout.getArraySize(); i++) {
//            workout.setExerciseListItem(i, exercise);
//        }

//        // creates a collection with the collection id named after the workoutTitle
//        Workout generatedWorkout = new Workout(currentUser.getClientName(), currentUser.getEmail(), exerciseName.get(), exerciseNum.get(), reps.get(),
//                day.get());
    }

    // sets user values
    public void setExerciseValues(String exerciseName, String exerciseReps) {
        this.exerciseName = exerciseName;
        this.exerciseReps = exerciseReps;
    }
}