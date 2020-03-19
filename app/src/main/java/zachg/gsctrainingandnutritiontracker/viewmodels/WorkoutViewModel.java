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

public class WorkoutViewModel extends ViewModel {

    private FirestoreRepository repo = new FirestoreRepository();
    public MutableLiveData<Workout> workoutLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Exercise>> exercises = new MutableLiveData<>();

    public MutableLiveData<String> generatedExerciseName = new MutableLiveData<String>();
    public MutableLiveData<String> generatedExerciseReps = new MutableLiveData<String>();
    public MutableLiveData<String> generatedExerciseWeight = new MutableLiveData<String>();
    public MutableLiveData<String> workoutTitleLiveData = new MutableLiveData<String>();
    public MutableLiveData<String> onError = new MutableLiveData<>();

    public ObservableField<String> etGeneratedExerciseName = new ObservableField<String>();
    public ObservableField<String> etGeneratedExerciseReps = new ObservableField<String>();
    public ObservableField<String> etGeneratedExerciseWeight = new ObservableField<String>();
    public ObservableField<String> etWorkoutTitle = new ObservableField<>();

    public Workout workout = new Workout();
    public Exercise exercise = new Exercise();
    public ArrayList<Exercise> exerciseArrayList = new ArrayList<>();
    public User client = new User();
    public String TAG = "WorkoutViewModel";
    public String dateString;
    public StringBuilder exerciseStringBuilder = new StringBuilder(5000);
    public String workoutTitle;

    public Exercise exercise2 = new Exercise();
    public Exercise exercise3 = new Exercise();
    public Exercise exercise4 = new Exercise();
    public Exercise exercise5 = new Exercise();

    public WorkoutViewModel() {}

    public void init(User user, Workout workout) {
        repo = FirestoreRepository.getInstance();
        this.client = user;
        this.workout = workout;
        this.workoutTitle = workout.getWorkoutTitle();
    }

    // Currently deletes all values
    // Writes report to the Repository
    public void updateWorkout(User client, Workout workout) {
        // getEtValues
        Workout updatedWorkout = new Workout(workout.getClientName(), client.getEmail(),
                workout.getWorkoutTitle());
        repo.updateWorkout(client, updatedWorkout);
    }

    public void deleteWorkout(User client, Workout workout) {
        repo.deleteWorkout(client, workout);
        // go back to workoutlist
    }
}
