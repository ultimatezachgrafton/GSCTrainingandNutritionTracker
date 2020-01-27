package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class ReportListViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo = new FirestoreRepository();
    private User currentUser = new User();
    private Report report = new Report();
    private MutableLiveData<Report> reports = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    public String TAG = "ReportListViewModel";

    public void init(User user) {
        this.currentUser = user;
        repo = FirestoreRepository.getInstance();
        String dateString = "";
        repo.setSnapshotOnCompleteListener(this);
    }

    public MutableLiveData<Report> getReports() {
        return reports;
    }

    public MutableLiveData<Boolean> getIsUpdating() {
        return isUpdating;
    }

    public Report onItemClicked(DocumentSnapshot documentSnapshot, int position) {
        // Fetches currentUser
        Report currentReport = documentSnapshot.toObject(Report.class);
        String id = documentSnapshot.getId();
        String path = documentSnapshot.getReference().getPath();
        return currentReport;
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot doc : task.getResult()) {
                if (doc.exists()) {
                    report = doc.toObject(Report.class);
                    reports.setValue(report);
                }
            }
        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
            return;
        }
    }
}