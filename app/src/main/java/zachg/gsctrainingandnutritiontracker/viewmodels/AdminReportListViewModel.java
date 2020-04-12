package zachg.gsctrainingandnutritiontracker.viewmodels;

import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.SingleLiveEvent;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class AdminReportListViewModel extends ViewModel {

    private FirestoreRepository repo = new FirestoreRepository();
    private User currentUser = new User();
    private Report report = new Report();
    private SingleLiveEvent<FirestoreRecyclerOptions<Report>> reports = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> isUpdating = new SingleLiveEvent<>();
    public String TAG = "AdminReportListViewModel";

    public void init(User user, Report report) {
        this.currentUser = user;
        this.report = report;
        reports.setValue(repo.getReportsFromRepo(currentUser));
    }

    public SingleLiveEvent<FirestoreRecyclerOptions<Report>> getReports() {
        return reports;
    }

    public SingleLiveEvent<Boolean> getIsUpdating() {
        return isUpdating;
    }

    public Report onItemClicked(DocumentSnapshot documentSnapshot, int position) {
        // Fetches report
        report = documentSnapshot.toObject(Report.class);
        String id = documentSnapshot.getId();
        String path = documentSnapshot.getReference().getPath();
        return report;
    }

}
