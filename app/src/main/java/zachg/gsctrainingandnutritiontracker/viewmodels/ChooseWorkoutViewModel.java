package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ChooseWorkoutViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {
    private FirestoreRepository repo;
    private User currentUser = new User();
    public ArrayList<Workout> workoutArray = new ArrayList<>();
    public final ObservableArrayList<String> workoutItems = new ObservableArrayList<>();
    public MutableLiveData<Workout> workoutLiveData = new MutableLiveData<>();
    public String TAG = "ChooseWorkoutViewModel";

    public ChooseWorkoutViewModel(User user) {
        repo = FirestoreRepository.getInstance();
        this.currentUser = user;
        workoutArray = repo.getWorkoutListFromRepo(currentUser);

        Log.d(TAG, "pre-array: " + workoutArray.size());

        // adding workoutarray items to workoutItems to be observed
        for (int i = 0; i < workoutArray.size(); i++) {
            workoutItems.add(String.valueOf(workoutArray.get(i).getExerciseName()));
//            Log.d(TAG, workoutArray.get(i).getExerciseName());
        }
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot doc : task.getResult()) {
                Workout workout = doc.toObject(Workout.class);
                workoutArray.add(workout);
                workoutLiveData.setValue(workout);
            }
        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
        }
    }
}
