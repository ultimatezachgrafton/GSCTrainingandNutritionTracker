package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import zachg.gsctrainingandnutritiontracker.fragments.ClientPortalFragment;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class ClientPortalViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo;

    public MutableLiveData<Report> reportLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> doesReportExist = new MutableLiveData<>();
    private User currentUser = new User();
    private Report report = new Report();
    public String TAG = "ClientPortalViewModel";
    private String dateGreeting;

    public void init() {
        repo = FirestoreRepository.getInstance();
    }

    public void getReportFromRepo(User currentUser, String dateString) {
        Log.d(TAG, "in viewmodel");
        repo.setQuerySnapshotOnCompleteListener(this);
        repo.getReportForPortal(currentUser, dateString);
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        QuerySnapshot qs = task.getResult();
        if (qs.size() == 0) {
            doesReportExist.setValue(false);
        } else {
            for (QueryDocumentSnapshot doc : qs) {
                report = doc.toObject(Report.class);
                reportLiveData.setValue(report);
                return;
            }
        }
    }
}