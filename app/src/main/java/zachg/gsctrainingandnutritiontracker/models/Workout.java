package zachg.gsctrainingandnutritiontracker.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

public class Workout extends BaseObservable {

    private String email;
    private String clientName, exerciseName, exerciseNum, reps, exerciseWeight, day;
    private int selectedItemPosition;
    private User currentUser = new User();

    public Workout(User user){
        this.currentUser = user;
        this.clientName = user.getClientName();
    }

    public Workout(String clientName, String email, String exerciseName, String exerciseNum, String reps, String day) {
        this.clientName = clientName;
        this.email = email;
        this.exerciseName = exerciseName;
        this.exerciseNum = exerciseNum;
        this.reps = reps;
        this.day = day;
    }

    public Workout() {}

    public String getClientName() {
        this.clientName = currentUser.getClientName();
        return clientName;
    }

    public void setClientName(String username) { clientName = username; }

    @Bindable
    public String getExerciseName() { return exerciseName; }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getReps() { return reps; }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public String getExerciseWeight() {
        return this.exerciseWeight;
    }

    public void setExerciseWeight(String exerciseWeight) {
        this.exerciseWeight = exerciseWeight;
    }

    public String getDay() { return day; }

    public void setDay(String day) { this.day = day; }

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