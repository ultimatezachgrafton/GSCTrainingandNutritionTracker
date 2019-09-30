package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;
import android.widget.ImageView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.File;
import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class AdminReportViewModel extends ViewModel {

    private FirestoreRepository repo;
    private Report currentReport = new Report();
    private User currentUser = new User();
    private MutableLiveData<Report> report = new MutableLiveData<>();

    private File photoFile;
    private ImageView photoView;

    // init getting null data for user
    public void init(User user, Report report) {
        final StringBuilder str = new StringBuilder();
        this.currentUser = user;
        this.currentReport = report;
        repo = FirestoreRepository.getInstance();

        DocumentReference docRef = repo.getReportByDate(currentUser, currentReport.getDateString());
        final ArrayList<Workout> workoutList = repo.getWorkoutListFromRepo(currentUser);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // for every workout in workoutlist, add values to the fullReport String
                for (int i = 0; i < workoutList.size(); i++) {
                    String s = String.valueOf(workoutList.get(i).getExerciseName());
                    str.append(s + "\n");
                }

                // TODO: don't let this return null
                Report report = documentSnapshot.toObject(Report.class);
                currentReport.setClientName(report.getClientName());
                currentReport.setDailyWeight(report.getDailyWeight());
                currentReport.setFullReport(String.valueOf(str));
            }
        });
    }

    public MutableLiveData<Report> getReport() { return report; }
}