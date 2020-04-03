package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.SingleLiveEvent;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class SelectWorkoutListViewModel extends ViewModel {

    private FirestoreRepository repo = new FirestoreRepository();
    private User client = new User();
    private Workout workout = new Workout();
    private MutableLiveData<FirestoreRecyclerOptions<Workout>> workouts = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    public String TAG = "WorkoutListViewModel";

    public void init(User client) {
        this.client = client;
        repo = FirestoreRepository.getInstance();
        repo.getWorkoutsFromRepo(client);
        workouts.setValue(repo.getWorkoutsFromRepo(client));
    }

    public MutableLiveData<FirestoreRecyclerOptions<Workout>> getWorkouts() {
        return workouts;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }

    public Workout onItemClicked(DocumentSnapshot documentSnapshot, int position) {
        // Fetches currentWorkout
        workout = documentSnapshot.toObject(Workout.class);
        String id = documentSnapshot.getId();
        String path = documentSnapshot.getReference().getPath();
        return workout;
    }
}
