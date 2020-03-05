package zachg.gsctrainingandnutritiontracker.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class Exercise extends BaseObservable {

    private String email;
    private String exerciseName, exerciseNum, reps, exerciseWeight;
    private int day;
    private int selectedItemPosition;

    public Exercise(String exerciseName, String reps, String weight) {
        this.exerciseName = exerciseName;
        this.reps = reps;
        this.exerciseWeight = weight;
    }

    public Exercise(String exerciseName, String exerciseNum, String reps, String exerciseWeight, int day){
        this.exerciseName = exerciseName;
        this.exerciseNum = exerciseNum;
        this.reps = reps;
        this.exerciseWeight = exerciseWeight;
        this.day = day;
    }

    public Exercise() {}

    public Exercise(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    @Bindable
    public String getExerciseName() { return exerciseName; }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
        notifyPropertyChanged(BR.exerciseName);
    }

    @Bindable
    public String getReps() { return reps; }

    public void setReps(String reps) {
        this.reps = reps;
        notifyPropertyChanged(BR.exerciseReps);
    }

    @Bindable
    public String getExerciseWeight() {
        return this.exerciseWeight;
    }

    public void setExerciseWeight(String exerciseWeight) {
        this.exerciseWeight = exerciseWeight;
        notifyPropertyChanged(BR.exerciseWeight);
    }

//    public int getDay() { return day; }
//
//    public void setDay(int day) { this.day = day; }

    public String getExerciseNum() { return exerciseNum; }

    public void setExerciseNum(String exerciseNum) { this.exerciseNum = exerciseNum; }

    @Bindable
    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    public void setSelectedItemPosition(int selectedItemPosition) {
        this.selectedItemPosition = selectedItemPosition;
        notifyPropertyChanged(BR.selectedItemPosition);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

}