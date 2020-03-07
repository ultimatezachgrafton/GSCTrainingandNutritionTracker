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
    public MutableLiveData<Integer> noReport = new MutableLiveData<Integer>();
    private User currentUser = new User();
    private Report report = new Report();
    public String TAG = "ClientPortalViewModel";
    private String dateGreeting;

    public void init(User user) {
        this.currentUser = user;
        repo = FirestoreRepository.getInstance();
        repo.setSnapshotOnCompleteListener(this);
        repo.getReportByUser(user);
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot doc : task.getResult()) {
                if (doc.exists()) {
                    report = doc.toObject(Report.class);
                    reportLiveData.setValue(report);
                } else {
                    Log.d(TAG, "no report");
                    noReport.setValue(1);
                }
            }
        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
            return;
        }
    }
}