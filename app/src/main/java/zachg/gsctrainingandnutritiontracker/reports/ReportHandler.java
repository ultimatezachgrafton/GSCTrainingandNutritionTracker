package zachg.gsctrainingandnutritiontracker.reports;

import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static zachg.gsctrainingandnutritiontracker.AdminList.AdminListFragment.currentSelectedUser;
import static zachg.gsctrainingandnutritiontracker.AdminList.UserHandler.db;
import static zachg.gsctrainingandnutritiontracker.calendar.DatePickerFragment.currentSelectedDate;
import static zachg.gsctrainingandnutritiontracker.calendar.DatePickerFragment.currentSelectedReport;

public class ReportHandler {
    public static final CollectionReference reportsColRef = db.collection("reports");
    public static final CollectionReference workoutsColRef = db.collection("workouts");
    public static final ArrayList<Report> mReports = new ArrayList<>();
    public static Workout currentSelectedWorkout = new Workout();
    private static int currentReportElement = 0; // the element of mReports containing currentSelectedReport

    public static ArrayList<Report> fetchReportsByUserDate(String date) {
        // TODO: date needs to match date format

        Query reportQuery = reportsColRef.whereEqualTo("clientName", currentSelectedUser.getClientName());

        // TODO: if report does not exist, create it

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
                    Log.d("mReports", "failed to make mReports");
                }
            }
        });

        // loop through mReports until find the one matching today
        for (int i = 0; i < mReports.size(); i++) {
            if (mReports.get(i).getDate() == currentSelectedDate) {
                currentSelectedReport = mReports.get(i);
                currentReportElement = i;
                break;
            }
        }

        Log.d("mReports", String.valueOf(mReports));
        Log.d("mReports", String.valueOf(currentSelectedReport));
        return mReports;
    }

    public static ArrayList<Workout> getWorkouts(final ArrayList<Workout> mWorkouts) {
        Query workoutQuery = workoutsColRef.document(currentSelectedUser.getClientName()).collection("workouts");

        workoutQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Workout workout = new Workout();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        workout = doc.toObject(Workout.class);
                        mWorkouts.add(workout);
                    }
                } else {
                    Log.d("mReports", "failed to make mWorkouts");
                }
            }
        });

        Log.d("mReports", "mWorkouts: " + String.valueOf(mWorkouts));
        // problem: mWorkouts has 0 elements
        updateWorkouts(mWorkouts);

        return mWorkouts;
    }

    public static void updateWorkouts(final ArrayList<Workout> mWorkouts) {
        if (currentSelectedUser.getPrevWorkoutNum() == 0) {
            currentSelectedWorkout = mWorkouts.get(1);
        } else if (mWorkouts.get(currentSelectedUser.getPrevWorkoutNum() + 1) == null) { // or if element is one more than getSize
            currentSelectedWorkout = mWorkouts.get(1);
        } else {
            currentSelectedWorkout = mWorkouts.get(currentSelectedUser.getPrevWorkoutNum() + 1);
        }
        currentSelectedUser.setPrevWorkoutNum(currentSelectedUser.getPrevWorkoutNum() + 1);
    }


    public static FirestoreRecyclerOptions<Report> getReportOptions(Query query) {
        FirestoreRecyclerOptions<Report> reportOptions = new FirestoreRecyclerOptions.Builder<Report>()
                .setQuery(query, Report.class)
                .build();

        return reportOptions;
    }
}