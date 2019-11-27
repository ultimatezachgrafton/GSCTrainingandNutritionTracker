package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
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

    public ReportViewModel() {}

    // init getting null data for user
    public void init(FirestoreRecyclerOptions<Workout> options) {
        repo = FirestoreRepository.getInstance();
        this.workouts = options;
    }

    // TODO: iterateWorkouts after loading this workout;

    // Writes report to the Repository
    public void writeReport(Report report) {
        dailyWeight.set(report.getDailyWeight());

        Log.d("mReports", String.valueOf(dailyWeight.get()));

        comments.set(report.getComments());
        Report generatedReport = new Report(report.getClientName(), report.getDate(), String.valueOf(dailyWeight.get()), String.valueOf(comments.get()));

        repo.db.collection("users").document(report.getClientName()).collection("reports")
                .document(String.valueOf(report.getDate()))
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