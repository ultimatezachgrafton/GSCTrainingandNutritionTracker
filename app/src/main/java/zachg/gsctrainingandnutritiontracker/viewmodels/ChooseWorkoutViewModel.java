package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.databinding.ObservableArrayList;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ChooseWorkoutViewModel {
    private FirestoreRepository repo;
    private User currentUser = new User();
    public ArrayList<Workout> workoutArray = new ArrayList<>();
    public final ObservableArrayList<String> workoutItems = new ObservableArrayList<>();
    public String TAG = "ChooseWorkoutViewModel";

    public ChooseWorkoutViewModel(User user) {
        repo = FirestoreRepository.getInstance();
        this.currentUser = user;
        workoutArray = repo.getWorkoutListFromRepo(currentUser);

        Log.d(TAG, "pre-array: " + workoutArray.size());

        // TODO: deal with names being null
        // Is this an Async issue?

        // adding workoutarray items to workoutItems to be observed
        for (int i = 0; i < workoutArray.size(); i++) {
            workoutItems.add(String.valueOf(workoutArray.get(i).getExerciseName()));
//            Log.d(TAG, workoutArray.get(i).getExerciseName());
        }
    }
}
