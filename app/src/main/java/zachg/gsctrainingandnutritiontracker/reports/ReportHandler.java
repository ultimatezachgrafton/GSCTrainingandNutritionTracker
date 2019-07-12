package zachg.gsctrainingandnutritiontracker.reports;

import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ReportHandler {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static final CollectionReference reportColRef = db.collection("reports");

    public static ArrayList<Report> fetchReportsByUserDate(final ArrayList<Report> mReports, String user, String date) {

        Query reportQuery = reportColRef.whereEqualTo("user", user).whereEqualTo("reports/date", date);
        getReportOptions(reportColRef);

        reportQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Report report = new Report();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        report = doc.toObject(Report.class);
                        mReports.add(report);
                    }
                } else {
                    Log.e(TAG, "failed to make mUsers");
                }
            }
        });
        return mReports;
    }

    public static FirestoreRecyclerOptions<Report> getReportOptions(Query query) {
        FirestoreRecyclerOptions<Report> reportOptions = new FirestoreRecyclerOptions.Builder<Report>()
                .setQuery(query, Report.class)
                .build();

        return reportOptions;
    }
}