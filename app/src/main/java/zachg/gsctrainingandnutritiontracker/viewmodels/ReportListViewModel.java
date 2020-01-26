package zachg.gsctrainingandnutritiontracker.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class ReportListViewModel extends ViewModel {

    private FirestoreRepository repo = new FirestoreRepository();
    private User currentUser = new User();
    private MutableLiveData<FirestoreRecyclerOptions<Report>> reports = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUpdating = new MutableLiveData<>();
    public String TAG = "ReportListViewModel";

    public void init(User user) {
        this.currentUser = user;
        repo = FirestoreRepository.getInstance();
        String dateString = "";
        reports.setValue(repo.getReportsByUser(currentUser, dateString));
    }

    public MutableLiveData<FirestoreRecyclerOptions<Report>> getReports() {
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
}