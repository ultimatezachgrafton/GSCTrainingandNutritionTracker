package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AdminClientProfileViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo = FirestoreRepository.getInstance();

    public User client = new User();
    public String workoutTitle;
    public MutableLiveData<String> workoutTitleLiveData = new MutableLiveData<>();
    public MutableLiveData<String> onError = new MutableLiveData<>();

    public static String DUPLICATE_WORKOUT_TITLE = "Workout title already in use.";
    public static String WORKOUT_TITLE_NULL = "Workout title is null.";

    public AdminClientProfileViewModel() {}

    public void init() {
        repo = FirestoreRepository.getInstance();
    }

    public MutableLiveData<String> getWorkoutTitleLiveData() {
        return workoutTitleLiveData;
    }

    // Checks if WorkoutTitle is null
    public void nullWorkoutTitleCheck(User client, String workoutTitle) {
        if (workoutTitle == null) {
            onError.setValue(WORKOUT_TITLE_NULL);
            return;
        } else {
            this.workoutTitle = workoutTitle;
            duplicateWorkoutTitleCheck(client, workoutTitle);
        }
    }

    // Checks if workoutTitle is already used for this user
    public void duplicateWorkoutTitleCheck(User user, String workoutTitle) {
        repo.setQuerySnapshotOnCompleteListener(this);
        repo.duplicateWorkoutTitleCheck(user, workoutTitle);
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        QuerySnapshot qs = task.getResult();
        if (qs.size() > 0) {
            onError.setValue(DUPLICATE_WORKOUT_TITLE);
        } else {
            workoutTitleLiveData.setValue(workoutTitle);
            getWorkoutTitleLiveData();
        }
    }
}