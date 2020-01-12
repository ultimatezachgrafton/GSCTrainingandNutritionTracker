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

    public String exerciseName, exerciseReps, workoutTitle, day;

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
        setExerciseValues(exerciseName, exerciseReps);
        setWorkoutValues(workoutTitle);
        Exercise exercise = new Exercise(exerciseName, exerciseReps);
        Workout workout = new Workout(currentUser.getClientName(), currentUser.getEmail(), workoutTitle);
        repo.writeWorkoutsToRepo(currentUser, workout);
    }

    // sets user values
    public void setExerciseValues(String exerciseName, String exerciseReps) {
        this.exerciseName = exerciseName;
        this.exerciseReps = exerciseReps;
    }

    public void setWorkoutValues(String workoutTitle) {
        this.workoutTitle = workoutTitle;
    }
}