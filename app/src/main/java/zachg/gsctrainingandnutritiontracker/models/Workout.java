package zachg.gsctrainingandnutritiontracker.models;

public class Workout {

    private String clientName, exerciseName, exerciseNum, reps, exerciseWeight, day;
    private User currentUser = new User();

    public Workout(User user){
        this.currentUser = user;
        this.clientName = user.getClientName();
    }

    public Workout(String clientName, String exerciseName, String exerciseNum, String reps, String day) {
        this.clientName = clientName;
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

}