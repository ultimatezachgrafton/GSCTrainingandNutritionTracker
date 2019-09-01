package zachg.gsctrainingandnutritiontracker.models;

public class Workout {

    // The variable and method names in Workout are a little strange, but this is by design.
    // They are meant to be as brief and clear as possible to simplify the admin's manual-entry database in Firestore.
    // Though this, of course, means some of that work falls on the shoulders of the valiant coders.

    private String clientName;
    private String exName;
    private String exNumber;
    private int reps;
    private int mExerciseWeight;
    private int day;
    private User mCurrentSelectedUser = new User();

    public Workout(User user){
        this.clientName = mCurrentSelectedUser.getClientName();
    }

    public Workout() {}

    public String getClientName() {
        this.clientName = mCurrentSelectedUser.getClientName();
        return clientName;
    }

    public void setClientName(String username) { clientName = username; }

    public String getExName() {
        return exName;
    }

    public void setExName(String exerciseName) {
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

    public String getExNumber() { return exNumber; }

    public void setExNumber(String exNum) { exNumber = exNum; }

}