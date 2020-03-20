package zachg.gsctrainingandnutritiontracker.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class Exercise extends BaseObservable {

    private String email;
    private String exerciseName, exerciseNum, exerciseReps, exerciseWeight;
    private int day;
    private int selectedItemPosition;

    public Exercise(String exerciseName, String exerciseReps, String weight) {
        this.exerciseName = exerciseName;
        this.exerciseReps = exerciseReps;
        this.exerciseWeight = weight;
    }

    public Exercise(String exerciseName, String exerciseNum, String exerciseReps, String exerciseWeight, int day){
        this.exerciseName = exerciseName;
        this.exerciseNum = exerciseNum;
        this.exerciseReps = exerciseReps;
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
    public String getExerciseReps() { return exerciseReps; }

    public void setExerciseReps(String exerciseReps) {
        this.exerciseReps = exerciseReps;
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