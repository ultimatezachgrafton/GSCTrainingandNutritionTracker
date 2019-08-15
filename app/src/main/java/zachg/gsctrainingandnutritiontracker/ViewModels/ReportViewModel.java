package zachg.gsctrainingandnutritiontracker.ViewModels;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.Models.Report;
import zachg.gsctrainingandnutritiontracker.Models.Workout;
import zachg.gsctrainingandnutritiontracker.Repositories.FirestoreRepository;

import static zachg.gsctrainingandnutritiontracker.Repositories.FirestoreRepository.db;
import static zachg.gsctrainingandnutritiontracker.Repositories.FirestoreRepository.sWorkouts;
import static zachg.gsctrainingandnutritiontracker.UI.Fragments.AdminListFragment.currentSelectedUser;

public class ReportViewModel extends ViewModel {
    private FirestoreRepository mRepo;
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Workout>> mWorkouts;
    private CollectionReference reportColRef = db.collection("reports");
    private DocumentReference reportDocRef = reportColRef.document("clientBanana");
    public static Workout currentSelectedWorkout = new Workout();

    public void init() {
        if (mWorkouts != null) {
            return;
        }
        mRepo = FirestoreRepository.getInstance();
        mWorkouts = mRepo.getWorkouts();
        Log.d("mReports", "mWorkouts filled?" + String.valueOf(mWorkouts));
        calculateWorkoutDay();
        Log.d("mReports", "calculate successful? " + currentSelectedWorkout);
    }

    public void addNewValue(final Workout workout) {
        mIsUpdating.setValue(true);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                ArrayList<Workout> workouts = mWorkouts.getValue();
                workouts.add(workout);
                mWorkouts.postValue(workouts);
                mIsUpdating.postValue(false);
            }

            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public void writeReport() {

        Report report = new Report();
        reportDocRef.set(report, SetOptions.merge())
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

    }

    public LiveData<ArrayList<Workout>> getWorkouts() {
        return mWorkouts;
    }

    public LiveData<Boolean> getIsUpdating() {
        return mIsUpdating;
    }

    public Workout calculateWorkoutDay() {
        // determine what workout day it is
        Workout workout = new Workout();
        if (currentSelectedUser.getWorkoutNum() < sWorkouts.size()){
            workout.setWorkoutDay(currentSelectedUser.getWorkoutNum());
        } else {
            workout.setWorkoutDay(0);
        }
        currentSelectedUser.incrementWorkout();
        int i = currentSelectedUser.getWorkoutNum();
        currentSelectedWorkout = sWorkouts.get(i);
        return workout;
    }
}