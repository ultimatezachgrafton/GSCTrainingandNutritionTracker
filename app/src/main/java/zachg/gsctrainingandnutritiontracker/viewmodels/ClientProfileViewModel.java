package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class ClientProfileViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo;

    public MutableLiveData<Date> dateLiveData = new MutableLiveData<>();

    public MutableLiveData<Workout> workoutSelected = new MutableLiveData<>();
    public MutableLiveData<ArrayList<String>> workoutTitleLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Workout>> workoutLiveData = new MutableLiveData<>();
    private MutableLiveData<FirestoreRecyclerOptions<Report>> reports = new MutableLiveData<>();
    private MutableLiveData<String> date = new MutableLiveData<>();
    private User currentUser = new User();
    public String TAG = "ClientProfileViewModel";

    public void init(User user) {
        this.currentUser = user;
        this.currentUser.setClientName(user.getClientName());
        repo = FirestoreRepository.getInstance();
        repo.setSnapshotOnCompleteListener(this);

//        if (reports != null) {
//            return;
//        }
//        reports.setValue(repo.getReportsByUser(user));
    }

    public void getWorkouts(User user) {
        repo.getWorkoutsFromRepo(user);
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot doc : task.getResult()) {
                Workout workout = doc.toObject(Workout.class);
                //workoutArray.add(workout);
                Log.d(TAG, workout.getExerciseName());
            }
        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
            return;
        }
    }

}
