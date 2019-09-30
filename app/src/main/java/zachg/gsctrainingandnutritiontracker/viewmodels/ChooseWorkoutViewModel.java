package zachg.gsctrainingandnutritiontracker.viewmodels;

import androidx.databinding.ObservableArrayList;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class ChooseWorkoutViewModel {
    private FirestoreRepository repo;
    private User user = new User();
    public ArrayList<Workout> workoutArray = new ArrayList<>();
    public final ObservableArrayList<String> workoutItems = new ObservableArrayList<>();

    public ChooseWorkoutViewModel(User user) {
        repo = FirestoreRepository.getInstance();
        this.user = user;
        workoutArray = repo.getWorkoutListFromRepo(user);

        // TODO: don't let names be null
        for (int i = 0; i < workoutArray.size() - 1; i++) {
            //workoutArray.get(i).setExerciseName(workoutArray.get(i).getExerciseName());
            workoutItems.add(String.valueOf(workoutArray.get(i).getExerciseName()));
        }
    }
}
