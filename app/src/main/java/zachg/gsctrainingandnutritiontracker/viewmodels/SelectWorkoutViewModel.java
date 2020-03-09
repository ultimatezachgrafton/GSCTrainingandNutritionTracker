package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class SelectWorkoutViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo = new FirestoreRepository();
    public MutableLiveData<ArrayList<Workout>> workoutLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public Report report = new Report();
    public Workout workout = new Workout();
    public Exercise exercise = new Exercise();
    public ArrayList<Workout> workoutArrayList = new ArrayList<>();
    public User currentUser = new User();
    public String TAG = "WorkoutViewModel";

    public SelectWorkoutViewModel() {}

    public void init(User user) {
        repo = FirestoreRepository.getInstance();
        this.currentUser = user;
        repo.setQuerySnapshotOnCompleteListener(this);
        repo.getWorkoutsForReport(currentUser);
    }

    public MutableLiveData<ArrayList<Workout>> getWorkouts() {
//        Log.d(TAG, "get workouts" + workoutArrayList.get(0).getWorkoutTitle());
        return workoutLiveData;
    }

//    public void iterateWorkoutNum(User user) {
//        if (user.getWorkoutDay()+1 < exerciseArrayList.size()) {
//            user.setWorkoutDay(user.getWorkoutDay() + 1);
//        } else {
//            user.setWorkoutDay(1);
//        }
//    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot doc : task.getResult()) {
                for (int i = 0; i <  task.getResult().size(); i++) {
                    Workout workout = doc.toObject(Workout.class);
                    workoutArrayList.add(workout);
                    Log.d(TAG, "adding workout" + workoutArrayList.get(i).getWorkoutTitle());
                }
                Log.d(TAG, workoutArrayList.get(0).getWorkoutTitle());
                workoutLiveData.setValue(workoutArrayList);
            }
        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
        }
    }
}
