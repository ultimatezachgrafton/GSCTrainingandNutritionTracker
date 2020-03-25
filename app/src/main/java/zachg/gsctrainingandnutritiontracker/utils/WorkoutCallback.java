package zachg.gsctrainingandnutritiontracker.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class WorkoutCallback implements OnCompleteListener<QuerySnapshot> {

    FirestoreRepository repo = new FirestoreRepository();
    private Exercise exercise = new Exercise();
    private Workout workout = new Workout();
    private MutableLiveData<FirestoreRecyclerOptions<Exercise>> exerciseLiveData;

    public void getExercisesFromRepo(User user, Workout workout) {
        repo.getExercisesFromRepo(user, workout);
    }

    public MutableLiveData<FirestoreRecyclerOptions<Exercise>> getExerciseLiveData() {
        return exerciseLiveData;
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot doc : task.getResult()) {
            }
        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
        }
    }
}
