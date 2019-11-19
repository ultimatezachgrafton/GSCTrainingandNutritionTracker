package zachg.gsctrainingandnutritiontracker.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;
import zachg.gsctrainingandnutritiontracker.ui.fragments.ClientProfileFragment;

public class ChooseWorkoutViewModel extends ViewModel {
    private FirestoreRepository repo;
    private User currentUser = new User();
    public ArrayList<Workout> workoutArray = new ArrayList<>();
    public ArrayList<String> workoutTitleArray = new ArrayList<>();
    public MutableLiveData<ArrayList<String>> workoutTitleLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Workout>> workoutLiveData = new MutableLiveData<>();
    public MutableLiveData workoutSelected = new MutableLiveData<>();
    public String TAG = "ChooseWorkoutViewModel";

    public ChooseWorkoutViewModel(User user) {
        repo = FirestoreRepository.getInstance();
        this.currentUser = user;
        createSpinnerItems();
    }

    public void createSpinnerItems() {
        for (int i = 0; i < workoutArray.size(); i++) {
            workoutTitleArray.set(i, workoutArray.get(i).getExerciseName());
        }
    }

    // set adapter for title spinner items here


}
