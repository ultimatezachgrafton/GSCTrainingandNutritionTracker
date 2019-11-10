package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class AdminClientProfileViewModel extends ViewModel {

    private FirestoreRepository repo;
    private User currentUser = new User();

    private ObservableField<String> exerciseName = new ObservableField<>();
    private ObservableField<String> exerciseNum = new ObservableField<>();
    private ObservableField<String> reps = new ObservableField<>();
    private ObservableField<String> day = new ObservableField<>();

    public AdminClientProfileViewModel() {
        // required empty constructor
    }

    public AdminClientProfileViewModel(User user) {
        this.currentUser = user;
        this.currentUser.setClientName(user.getClientName());
    }

    public void init() {
        repo = FirestoreRepository.getInstance();
    }

    public void writeToWorkouts() {
        Workout workout = new Workout();
        exerciseName.set(workout.getExerciseName());
        exerciseNum.set(workout.getExerciseNum());
        reps.set(workout.getReps());
        day.set(workout.getDay());

        // TODO: getClientEmail, not name
        Workout generatedWorkout = new Workout(currentUser.getClientName(), currentUser.getEmail(), exerciseName.get(), exerciseNum.get(), reps.get(),
                day.get());

        repo.writeWorkoutsToRepo(generatedWorkout);
    }
}