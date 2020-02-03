package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import zachg.gsctrainingandnutritiontracker.R;
import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.models.Workout;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;
import zachg.gsctrainingandnutritiontracker.ui.activities.SingleFragmentActivity;
import zachg.gsctrainingandnutritiontracker.ui.fragments.ViewReportFragment;

public class ClientProfileViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo;

    public MutableLiveData<Report> reportLiveData = new MutableLiveData<>();
    private User currentUser = new User();
    private Report report = new Report();
    public String TAG = "ClientProfileViewModel";

    public void init(User user) {
        this.currentUser = user;
        repo = FirestoreRepository.getInstance();
        repo.setSnapshotOnCompleteListener(this);
    }

    public void getReportByUser(User user) {
        repo.getReportByUser(user); }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot doc : task.getResult()) {
                if (doc.exists()) {
                    report = doc.toObject(Report.class);
                    reportLiveData.setValue(report);
                }
            }
        } else {
            Log.d(TAG, "Error getting documents: ", task.getException());
            return;
        }
    }
}