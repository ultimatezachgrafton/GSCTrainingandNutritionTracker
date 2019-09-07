package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class ReportViewModel extends ViewModel {

    private FirestoreRepository repo;
    private ObservableField<String> dailyWeight = new ObservableField<>();
    private ObservableField<String> comments = new ObservableField<>();
    private FirestoreRecyclerOptions<Workout> workouts;
    private Workout workout = new Workout();

    public ReportViewModel() {}

    // init getting null data for user
    public void init(FirestoreRecyclerOptions<Workout> options) {
        repo = FirestoreRepository.getInstance();
        this.workouts = options;
        //this.workout = currentWorkout;
    }

    // TODO: iterateWorkouts after loading this workout;

    // Writes report to the Repository
    public void writeReport(Report report) {
        dailyWeight.set(report.getDailyWeight());
        comments.set(report.getComments());

        // workoutOptions.get();
        // getWorkouts();

//        List<Workout> workoutList = new ArrayList<>();
//        for (int i = 0; i < MAX_WORKOUTS; i++) {
//            workoutList.add(new Workout(i));
//        }

        Report generatedReport = new Report(report.getClientName(), report.getDateString(), String.valueOf(dailyWeight.get()), String.valueOf(comments.get()));

        repo.db.collection("users").document(report.getClientName()).collection("reports")
                .document(report.getDateString())
                .set(generatedReport)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("reports", "DocumentSnapshot added with ID: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("reports", "Error writing document", e);
                    }
                });
        // TODO: write iterated workoutNum to user's fstore data
    }
}