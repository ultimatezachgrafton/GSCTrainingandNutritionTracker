package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import zachg.gsctrainingandnutritiontracker.fragments.ClientPortalFragment;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.SingleLiveEvent;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class ClientPortalViewModel extends ViewModel implements OnCompleteListener<DocumentSnapshot> {

    private FirestoreRepository repo;

    private SingleLiveEvent<Report> existingReport = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> doesReportExist = new SingleLiveEvent<>();
    private SingleLiveEvent<String> onError = new SingleLiveEvent<>();
    private User user = new User();
    private Report report = new Report();
    public String TAG = "ClientPortalViewModel";
    private String dateGreeting;
    private String NO_REPORT = "No report for this date.";

    public void init(User user) {
        this.user = user;
    }

    public SingleLiveEvent<String> getOnError() { return onError; }

    public void getReportFromRepo(User user, String dateString) {
        repo.setDocumentReferenceOnCompleteListener(this);
        repo.getReportForPortal(user, dateString);
    }

    @Override
    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        DocumentSnapshot doc = task.getResult();
        if (!doc.exists()) {
            doesReportExist.setValue(false);
        } else {
           report = doc.toObject(Report.class);
           assignReportValue(report);
           return;
        }
    }

    public void assignReportValue(Report report) {
        existingReport.setValue(report);
    }

    public SingleLiveEvent<Report> getExistingReport() { return existingReport; }

    public SingleLiveEvent<Boolean> getDoesReportExist() {
        return doesReportExist;
    }
}