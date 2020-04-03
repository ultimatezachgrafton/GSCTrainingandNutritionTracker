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
import zachg.gsctrainingandnutritiontracker.models.SingleLiveEvent;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class ClientPortalViewModel extends ViewModel implements OnCompleteListener<QuerySnapshot> {

    private FirestoreRepository repo;

    private SingleLiveEvent<Report> reportSingleLiveEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> doesReportExist = new SingleLiveEvent<>();
    private SingleLiveEvent<String> onError = new SingleLiveEvent<>();
    private User user = new User();
    private Report report = new Report();
    public String TAG = "ClientPortalViewModel";
    private String dateGreeting;
    private String NO_REPORT = "No report for this date.";

    public void init(User user) {
        repo = FirestoreRepository.getInstance();
        this.user = user;
    }

    public SingleLiveEvent<String> getOnError() { return onError; }

    public void getReportFromRepo(User user, String dateString) {
        repo.setQuerySnapshotOnCompleteListener(this);
        repo.getReportForPortal(user, dateString);
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        QuerySnapshot qs = task.getResult();
        if (qs.size() == 0) {
            doesReportExist.setValue(false);
        } else {
            for (QueryDocumentSnapshot doc : qs) {
                report = doc.toObject(Report.class);
                assignReportValue(report);
                return;
            }
        }
    }

    public void createDateString(int year, int month, int dayOfMonth) {
        String dayOfMonthStr, monthStr;
        // if dayOfMonth is less than 10, put a zero in front of it
        if (dayOfMonth < 10) {
            dayOfMonthStr = "0" + (dayOfMonth);
        } else {
            dayOfMonthStr = String.valueOf(dayOfMonth);
        }
        if (month < 10) {
            monthStr = "0" + (month + 1);
        } else {
            monthStr = String.valueOf(month);
        }
        String dateString = (monthStr + "-" + dayOfMonthStr + "-" + year);
        this.report.setDateString(dateString);
        getReportFromRepo(user, dateString);
    }

    public void assignReportValue(Report report) {
        reportSingleLiveEvent.setValue(report);
    }

    public SingleLiveEvent<Report> getReportSingleLiveEvent() {
        return reportSingleLiveEvent;
    }

    public SingleLiveEvent<Boolean> getDoesReportExist() {
        return doesReportExist;
    }
}