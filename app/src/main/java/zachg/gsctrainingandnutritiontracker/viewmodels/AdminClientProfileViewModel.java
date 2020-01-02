package zachg.gsctrainingandnutritiontracker.viewmodels;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class AdminClientProfileViewModel extends ViewModel {

    private FirestoreRepository repo;
    private User currentUser = new User();

    private ObservableField<String> exerciseName = new ObservableField<>();
    private ObservableField<String> workoutTitle = new ObservableField<>();
    private ObservableField<String> reps = new ObservableField<>();
    private ObservableField<String> day = new ObservableField<>();

    public AdminClientProfileViewModel() {}

    public AdminClientProfileViewModel(User user) {
        this.currentUser = user;
        this.currentUser.setClientName(user.getFirstName(), user.getLastName());
    }

    public void init() {
        repo = FirestoreRepository.getInstance();
    }

    public void writeToWorkouts() {
        Workout workout = new Workout();
        Exercise exercise = new Exercise();
        exerciseName.set(exercise.getExerciseName());
        reps.set(exercise.getReps());
        day.set(exercise.getDay());

        workout.setWorkoutTitle(exercise.getExerciseName());
        workout.setExerciseListItem(exercise);
        repo.writeWorkoutsToRepo(currentUser, workout);

        // TODO: write exercise to workout array at value == exerciseNum
//        for (int i = 0; i < workout.getArraySize(); i++) {
//            workout.setExerciseListItem(i, exercise);
//        }

//        // creates a collection with the collection id named after the workoutTitle
//        Workout generatedWorkout = new Workout(currentUser.getClientName(), currentUser.getEmail(), exerciseName.get(), exerciseNum.get(), reps.get(),
//                day.get());
    }
}