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

import java.lang.reflect.Array;
import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class WorkoutViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo = new FirestoreRepository();
    public MutableLiveData<Workout> workoutLiveData = new MutableLiveData<>();
    public MutableLiveData<FirestoreRecyclerOptions<Exercise>> exerciseLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Exercise>> exercises = new MutableLiveData<>();
    public MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();

    public ObservableField<String> exerciseName = new ObservableField<>();
    public ObservableField<String> exerciseReps = new ObservableField<>();
    public ObservableField<String> exerciseWeight = new ObservableField<>();

    public Workout workout = new Workout();
    public Exercise exercise = new Exercise();
    public ArrayList<Exercise> exerciseArrayList = new ArrayList<>();
    public User client = new User();
    public String TAG = "WorkoutViewModel";
    public String dateString;
    public StringBuilder exerciseStringBuilder = new StringBuilder(5000);
    public String workoutTitle;
//    public int workoutDay;

    public WorkoutViewModel() {}

    public void init(User user, Workout workout) {
        repo = FirestoreRepository.getInstance();
        this.client = user;
        this.workout = workout;
        this.workoutTitle = workout.getWorkoutTitle();
//        workoutDay = currentUser.getWorkoutDay();
        repo.setSnapshotOnCompleteListener(this);
//        exerciseLiveData.setValue();
        repo.getExercisesFromRepo(client, workout);
    }

    public MutableLiveData<FirestoreRecyclerOptions<Exercise>> getExerciseLiveData() {
        return exerciseLiveData;
    }

    public MutableLiveData<ArrayList<Exercise>> getExercises() {
        return exercises;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }

    // Writes report to the Repository
    public void updateWorkout(User client, Workout workout) {
        Workout updatedWorkout = new Workout(workout.getClientName(), client.getEmail(),
                workout.getWorkoutTitle());
        repo.updateWorkout(client, updatedWorkout);
    }

    public void deleteWorkout(User client, Workout workout) {
        repo.deleteWorkout(client, workout);
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            Log.d(TAG, "size: " + String.valueOf(task.getResult().size()));
            for (QueryDocumentSnapshot doc : task.getResult()) {
                for (int i = 0; i <  task.getResult().size(); i++) {
                    Workout w = doc.toObject(Workout.class);
                    workout = w;
                    exercises.setValue(w.getExercises());
                }
            }
        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
        }
    }
}
