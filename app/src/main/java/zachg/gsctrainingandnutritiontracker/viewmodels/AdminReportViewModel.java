package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.util.Log;
import android.widget.ImageView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.File;

import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class AdminReportViewModel extends ViewModel {

    private FirestoreRepository repo;
    private Report currentReport = new Report();
    private User currentUser = new User();
    private MutableLiveData<Report> report = new MutableLiveData<>();

    private File photoFile;
    private ImageView photoView;

    // init getting null data for user
    public void init(User user, Report report) {
        this.currentUser = user;
        this.currentReport = report;
        //Report report = new Report(String clientName, String date, String dailyWeight, String comments)
        repo = FirestoreRepository.getInstance();
        DocumentReference docRef = repo.getReportByDate(currentUser, currentReport.getDateString());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // TODO: if report doesn't exist, don't let this return null
                Report report = documentSnapshot.toObject(Report.class);
                currentReport.setClientName(report.getClientName());
                currentReport.setDailyWeight(report.getDailyWeight());

                // put docsnap into List, then iterate through list via for loop to print the values
            }
        });
    }

    public MutableLiveData<Report> getReport() { return report; }
}