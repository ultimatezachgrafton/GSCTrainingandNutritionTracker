package zachg.gsctrainingandnutritiontracker.Models;

import android.util.Log;

import static zachg.gsctrainingandnutritiontracker.Repositories.FirestoreRepository.sWorkouts;
import static zachg.gsctrainingandnutritiontracker.UI.Fragments.AdminListFragment.currentSelectedUser;
import static zachg.gsctrainingandnutritiontracker.ViewModels.ReportViewModel.currentSelectedWorkout;

public class Workout {

    // The variable and method names in Workout are a little strange, but this is by design.
    // They are meant to be as brief and clear as possible to simplify the admin's manual-entry database in Firestore.
    // Though this, of course, means some of that work falls on the shoulders of the valiant coders.

    private String clientName;
    private String exName;
    private int reps;
    private int mExerciseWeight;
    private int day;

    public Workout(User user){
        this.clientName = currentSelectedUser.getClientName();
    }

    public Workout() {
        this.clientName = currentSelectedUser.getClientName();
    }

    public String getClientName() {
        this.clientName = currentSelectedUser.getClientName();
        return clientName;
    }

    public void setClientName(String username) { clientName = username; }

    public String getExerciseName() {
        return exName;
    }

    public void setExerciseName(String exerciseName) {
        exName = exerciseName;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int repsNum) {
        reps = repsNum;
    }

    public int getExerciseWeight() {
        return mExerciseWeight;
    }

    public void setExerciseWeight(int exerciseWeight) {
        mExerciseWeight = exerciseWeight;
    }

    public int getWorkoutDay() { return day; }

    public void setWorkoutDay(int prevNum) { day = prevNum + 1; }

}