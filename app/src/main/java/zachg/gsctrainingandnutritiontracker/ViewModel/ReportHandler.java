package zachg.gsctrainingandnutritiontracker.ViewModel;

import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import zachg.gsctrainingandnutritiontracker.Model.Report;
import zachg.gsctrainingandnutritiontracker.Model.Workout;
import zachg.gsctrainingandnutritiontracker.UI.Fragments.DatePickerFragment;
import static zachg.gsctrainingandnutritiontracker.UI.Fragments.AdminListFragment.currentSelectedUser;


public class ReportHandler {
    private DatePickerFragment dp = new DatePickerFragment();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public final CollectionReference reportsColRef = db.collection("reports");
    public final CollectionReference workoutsColRef = db.collection("workouts");
    public Query reportQuery = reportsColRef.whereEqualTo("clientName", currentSelectedUser.getClientName());
    public Workout currentSelectedWorkout = new Workout();
    public ArrayList<Workout> mWorkouts = new ArrayList<>();

    public ArrayList<Report> fetchReportsByUserDate(final ArrayList<Report> reports, String date) {
        // TODO: if report does not exist, create it

        reportQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("mReports", "hi reportQuery");
                    Report report = new Report();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Log.d("mReports", "bi reportQuery");
                        report = doc.toObject(Report.class);
                        reports.add(report);
                        Log.d("mReports", "report: "+report);
                        Log.d("mReports", "mReports: " + String.valueOf(reports));
                    }
                } else {
                    Log.d("mReports", "failed to make mReports");
                }
            }
        });

        // loop through mReports until find the one matching today
        for (int i = 0; i < reports.size(); i++) {
            if (reports.get(i).getDate() == date) {
                dp.currentSelectedReport = reports.get(i);
                break;
            }
        }

        Log.d("mReports", "mReports: " + String.valueOf(dp.mReports)); // returning empty
        Log.d("mReports", "currentSelectedReport: " + String.valueOf(dp.currentSelectedReport)); // has a value
        return reports;
    }

    public void writeReports(Report report, EditText etWeight) {

        String mDailyWeight = etWeight.getText().toString();
//      String mExerciseName = tvExerciseName.getText().toString();
//      String mWeightUsed = etWeightUsedEntry.getText().toString();
//      String mRepsEntry = tvRepsEntry.getText().toString();
//      String mWorkoutComments = etWorkoutComments.getText().toString();

        // Access a Cloud Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a Report
        Map<String, Object> rHash = new HashMap<>();
        rHash.put("client name", currentSelectedUser.getClientName());
        rHash.put("date", dp.currentSelectedDate);
        rHash.put("weight", mDailyWeight);
//      rHash.put("Weight Used ", mWeightUsed);
//      rHash.put("Workout comments:", mWorkoutComments);

        db.collection("reports").document("LA")
                .set(report, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("mReports", "DocumentSnapshot added with ID: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("mReports", "Error writing document", e);
                    }
                });

//                // Add user as a new document with a generated ID
//                db.collection("reports")
//                        .add(report)
//                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                @Override
//                                public void onSuccess(DocumentReference documentReference) {
//                                    Log.d("mReports", "DocumentSnapshot added with ID: " + documentReference.getId());
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.w("mReports", "Error adding document", e);
//                                }
//                            });
//            }
            // "Send Report" turns into "Update Report"
    }


    public ArrayList<Workout> getWorkouts(final ArrayList<Workout> workouts) {
        Query workoutQuery = workoutsColRef.document(currentSelectedUser.getClientName()).collection("workouts");
        // options for RecylerView display
        getWorkoutOptions(workoutQuery);

        // problem: there are no querydocumentsnapshots
        workoutQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Workout workout = new Workout();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        workout = doc.toObject(Workout.class);
                        Log.d("mReports", "workout: " + String.valueOf(workout));
                        workouts.add(workout);
                    }
                } else {
                    Log.d("mReports", "failed to make mWorkouts");
                }
            }
        });

        Log.d("mReports", "mWorkouts: " + String.valueOf(workouts));
        // problem: mWorkouts is null
        //updateWorkouts();

        return workouts;
    }

    public void updateWorkouts() {
        if (currentSelectedUser.getPrevWorkoutNum() == 0) {
            currentSelectedWorkout = mWorkouts.get(0);
        } else if (mWorkouts.get(currentSelectedUser.getPrevWorkoutNum() + 1) == null) { // or if element is one more than getSize
            currentSelectedWorkout = mWorkouts.get(1);
        } else {
            currentSelectedWorkout = mWorkouts.get(currentSelectedUser.getPrevWorkoutNum() + 1);
        }
        currentSelectedUser.setPrevWorkoutNum(currentSelectedUser.getPrevWorkoutNum() + 1);
    }


    public FirestoreRecyclerOptions<Report> getReportOptions(Query query) {
        FirestoreRecyclerOptions<Report> reportOptions = new FirestoreRecyclerOptions.Builder<Report>()
                .setQuery(query, Report.class)
                .build();

        return reportOptions;
    }

    public FirestoreRecyclerOptions<Workout> getWorkoutOptions(Query query) {
        FirestoreRecyclerOptions<Workout> workoutOptions = new FirestoreRecyclerOptions.Builder<Workout>()
                .setQuery(query, Workout.class)
                .build();

        return workoutOptions;
    }

}