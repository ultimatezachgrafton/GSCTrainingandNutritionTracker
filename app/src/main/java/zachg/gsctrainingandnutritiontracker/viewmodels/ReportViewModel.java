package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.SetOptions;

import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class ReportViewModel extends ViewModel {

    private FirestoreRepository mRepo;
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();
    private MutableLiveData<FirestoreRecyclerOptions<Workout>> mWorkouts = new MutableLiveData<>();

    public String mClientName;
    public String mDateString;

    public ReportViewModel() {}

    // init getting null data for user
    public void init(User mCurrentUser) {
        mRepo = FirestoreRepository.getInstance();
        mWorkouts.setValue(mRepo.getWorkoutsFromRepo(mCurrentUser));
    }

    // iterateWorkouts after loading this workout;

    // Writes report to the Repository
    public void writeReport(Report report, User user) {
        mRepo.db.collection("users").document(report.getClientName()).collection("reports")
                .document(report.getDateString())
                .set(report, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("mReports", "DocumentSnapshot added with ID: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("mReports", "Error writing document", e);
                    }
                });
        // write iterated workoutNum to user's fstore data
    }

    public MutableLiveData<FirestoreRecyclerOptions<Workout>> getWorkouts() {
        return mWorkouts;
    }

    public LiveData<Boolean> getIsUpdating() {
        return mIsUpdating;
    }
}