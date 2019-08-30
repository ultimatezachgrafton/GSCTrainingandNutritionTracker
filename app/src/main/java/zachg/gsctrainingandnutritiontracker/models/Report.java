package zachg.gsctrainingandnutritiontracker.models;

import android.widget.EditText;

import java.util.Date;

// Report is the class that will comprise the individual reports from clients

public class Report {

    private String reportId; // unique ID
    private Date date = new Date(); // date of report
    private String dateString; // date converted to string
    private String clientName; // client's name
    private String dailyWeight; // client's weight

    private String exerciseName; // name of exercise
    private int repsAmount; // amount of sets and reps assigned to exercise
    private int weightAmount; // amount of weight in lbs used in each set

    private EditText etCommentBox; // space for exercise-related comments
    private String comments;
    // photos to include with questions
    private boolean isNew; // determines if report is new

    // This constructor generates a universally unique ID, then initializes it as well as the Date field
    public Report() { }

    public Report(User user, Date date) {
        this.date = date;
        this.dateString = date.toString();
        this.clientName = user.getClientName();
    }

    // Setters and getters
    public String getReportId() {
            return reportId;
        }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateString() {
        dateString = date.toString();
        return dateString;
    }

    public void setDateString(Date date) {
            dateString = date.toString();
        }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String client) { clientName = client; }

    public String getDailyWeight() { return dailyWeight; }

    public void setWeight(String weight) { this.dailyWeight = weight; }

    public boolean getIsNew() { return isNew; }

    public void setIsNew(boolean isNew) { this.isNew = isNew; }

//    public String getPhotoFilename() { return "IMG_" + getId().toString() + ".jpg"; }

    public void setExerciseName(String exerciseName) { this.exerciseName = exerciseName; }

    public String getExerciseName() { return exerciseName; }

    public void setWeightAmount(int weightAmount) { this.weightAmount = weightAmount; }

    public int getWeightAmount() { return weightAmount; }

    public void setReps(int repsAmount) { this.repsAmount = repsAmount; }

    public int getReps() { return repsAmount; }

    public String getComments() { return comments; }

}