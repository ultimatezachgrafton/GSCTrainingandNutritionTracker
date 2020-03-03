package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class WorkoutListViewModel extends ViewModel {

    private FirestoreRepository repo = new FirestoreRepository();
    private User client = new User();
    private Workout workout = new Workout();
    private MutableLiveData<FirestoreRecyclerOptions<Workout>> workouts = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    public String TAG = "WorkoutListViewModel";

    public void init(User client) {
        this.client = client;
        repo = FirestoreRepository.getInstance();
        workouts.setValue(repo.getWorkoutsFromRepo(client));
        Log.d(TAG, String.valueOf(workouts.getValue()));
    }

    public MutableLiveData<FirestoreRecyclerOptions<Workout>> getWorkouts() {
        return workouts;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }

    public Workout onItemClicked(DocumentSnapshot documentSnapshot, int position) {
        // Fetches currentWorkout
        Workout currentWorkout = documentSnapshot.toObject(Workout.class);
        String id = documentSnapshot.getId();
        String path = documentSnapshot.getReference().getPath();
        return currentWorkout;
    }
}
