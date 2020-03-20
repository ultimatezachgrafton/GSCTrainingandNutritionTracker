package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.fragments.ReportListFragment;
import zachg.gsctrainingandnutritiontracker.fragments.WorkoutListFragment;
import zachg.gsctrainingandnutritiontracker.models.Exercise;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AdminClientProfileViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo = FirestoreRepository.getInstance();

    public User client = new User();
    public Workout workout = new Workout();
    public Exercise exercise = new Exercise();
    public ArrayList<Exercise> exerciseArray = new ArrayList<>();
    public MutableLiveData<String> workoutTitleLiveData = new MutableLiveData<String>();
    public MutableLiveData<String> onError = new MutableLiveData<>();

    public static String DUPLICATE_WORKOUT_TITLE = "Workout title already in use.";
    public static String WORKOUT_TITLE_NULL = "Workout title is null.";

    public AdminClientProfileViewModel() {}

    public void init(User client, Workout workout) {
        this.client = client;
        repo = FirestoreRepository.getInstance();
    }

    // Checks if WorkoutTitle is null
    public void nullWorkoutTitleCheck(User client, Workout workout) {
        if (workout.getWorkoutTitle().length() == 0) {
            onError.setValue(WORKOUT_TITLE_NULL);
            return;
        } else {
            duplicateWorkoutTitleCheck(client, workout, exerciseArray);
        }
    }

    // Checks if workoutTitle is already used for this user
    public void duplicateWorkoutTitleCheck(User user, Workout workout, ArrayList<Exercise> exerciseArray) {
        this.exerciseArray = exerciseArray;
        repo.setQuerySnapshotOnCompleteListener(this);
        repo.duplicateWorkoutTitleCheck(user, workout);
    }

    // Write workouts to the repo
    public void writeToWorkouts(User user, Workout workout, ArrayList<Exercise> exerciseArray) {

        workout.setClientName(user.getClientName());
        workout.setEmail(user.getEmail());
        workout.setExercises(exerciseArray);

        repo.writeWorkoutsToRepo(user, workout);
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        QuerySnapshot qs = task.getResult();
        if (qs.size() > 0) {
            onError.setValue(DUPLICATE_WORKOUT_TITLE);
        } else {
            writeToWorkouts(client, workout, exerciseArray);
        }
    }
}