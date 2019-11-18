package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class ViewReportViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo;
    private Report currentReport = new Report();
    private User currentUser = new User();
    private User client = new User();
    private MutableLiveData<Report> reportLiveData = new MutableLiveData<>();

    private File photoFile;
    private ImageView photoView;

    // init getting null data for user
    public void init(User user, Report report) {
        final StringBuilder str = new StringBuilder();
        this.client = user;
        this.currentReport = report;
        repo = FirestoreRepository.getInstance();
        repo.setSnapshotOnCompleteListener(this);
        repo.getWorkoutsFromRepo(client);
    }

    public MutableLiveData<Report> getReport() { return reportLiveData; }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot doc : task.getResult()) {
                Report report = doc.toObject(Report.class);
                reportLiveData.setValue(report);
            }
        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
        }
    }
}