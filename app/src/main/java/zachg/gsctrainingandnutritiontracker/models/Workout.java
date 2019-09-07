package zachg.gsctrainingandnutritiontracker.models;

public class Workout {

    private String clientName;
    private String exerciseName;
    private int exerciseNum;
    private String reps;
    private int exerciseWeight;
    private int day;
    private User currentUser = new User();

    public Workout(User user){
        this.clientName = currentUser.getClientName();
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

    public int getExerciseWeight() {
        return this.exerciseWeight;
    }

    public void setExerciseWeight(int exerciseWeight) {
        this.exerciseWeight = exerciseWeight;
    }

    public int getWorkoutDay() { return day; }

    public void setWorkoutDay(int prevNum) { day = prevNum + 1; }

    public int getExerciseNum() { return exerciseNum; }

    public void setExerciseNum(int exerciseNum) { this.exerciseNum = exerciseNum; }

}