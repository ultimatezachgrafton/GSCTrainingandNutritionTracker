package zachg.gsctrainingandnutritiontracker.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Workout extends BaseObservable {

    private String email, clientName, workoutTitle;
    private int workoutDay;
    private int selectedItemPosition;
    private boolean prevWorkout = false;
    private ArrayList<Exercise> exercises = new ArrayList<>();
    private Exercise exercise = new Exercise();
    private User currentUser = new User();

    public Workout(User user){
        this.currentUser = user;
        this.clientName = user.getClientName();
    }

    public Workout(String clientName, String email, String workoutTitle) {
        this.clientName = clientName;
        this.email = email;
        this.workoutTitle = workoutTitle;
    }

    public Workout() {}

    public String getClientName() {
        this.clientName = currentUser.getClientName();
        return clientName;
    }

    public void setClientName(String username) { clientName = username; }

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

    @Bindable
    public int getWorkoutDay() {
        return workoutDay;
    }

    public void setWorkoutDay(int workoutDay) {
        this.workoutDay = workoutDay;
        notifyPropertyChanged(BR.workoutDay);
    }

    @Bindable
    public String getWorkoutTitle() {
        return workoutTitle;
    }

    public void setWorkoutTitle(String workoutTitle) {
        this.workoutTitle = workoutTitle;
        notifyPropertyChanged(BR.workoutTitle);
    }

    @Bindable
    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
        notifyPropertyChanged(BR.exercise);
    }

    @Bindable
    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
        notifyPropertyChanged(BR.exercises);
    }

    public void setExerciseListItem(Exercise exercise) {
        exercises.add(exercise);
    }

    public int getArraySize() {
        return exercises.size();
    }

}