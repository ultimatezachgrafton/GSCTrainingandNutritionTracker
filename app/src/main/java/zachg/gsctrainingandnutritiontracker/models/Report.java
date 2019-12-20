package zachg.gsctrainingandnutritiontracker.models;

// Report is the class that will comprise the individual reports from clients

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.library.baseAdapters.BR;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Report extends BaseObservable {

    private String reportId; // unique ID
    private Date date;
    private String dateString;
    private String clientName; // client's name
    private String dailyWeight; // client's weight

    private String exerciseNum;
    private String exerciseReps;
    private String exerciseWeight;
    private String exerciseName;

    private String comments;
    private boolean isNew; // determines if report is new

    private ArrayList<Workout> workoutLists;
    private String fullReport;
    private String day;

    public Report() {}

    public Report(User user) {
        this.clientName = user.getClientName();
    }

    public Report(User user, Date date) {
        this.date = date;
        this.clientName = user.getClientName();
    }

    public Report(String clientName, String dailyWeight, String exerciseWeight, String comments, String dateString) {
        this.clientName = clientName;
        this.dailyWeight = dailyWeight;
        this.exerciseWeight = exerciseWeight;
        this.comments = comments;
        this.dateString = dateString;
    }

    @Bindable
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        notifyPropertyChanged(BR.date);
    }

    // date string to view in TextViews
    @Bindable
    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
        notifyPropertyChanged(BR.dateString);
    }

    @Bindable
    public String getComments() { return comments; }

    public void setComments(String comments) {
        this.comments = comments;
        notifyPropertyChanged(BR.comments);
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getExerciseNum() {
        return exerciseNum;
    }

    public void setExerciseNum(String exerciseNum) {
        this.exerciseNum = exerciseNum;
    }

    public String getExerciseReps() {
        return exerciseReps;
    }

    public void setExerciseReps(String reps) {
        this.exerciseReps = reps;
    }

    public String getExerciseWeight() {
        return exerciseWeight;
    }

    public void setExerciseWeight(String exerciseWeight) {
        this.exerciseWeight = exerciseWeight;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportId() {
            return reportId;
        }

    @Bindable
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String client) {
        clientName = client;
        notifyPropertyChanged(BR.clientName);
    }

    @Bindable
    public String getDailyWeight() { return dailyWeight; }

    public void setDailyWeight(String weight) {
        this.dailyWeight = weight;
        notifyPropertyChanged(BR.dailyWeight); }

    public boolean getIsNew() { return isNew; }

    public void setIsNew(boolean isNew) { this.isNew = isNew; }

//    public String getPhotoFilename() { return "IMG_" + getReportId().toString() + ".jpg"; }

    @Bindable
    public String getFullReport() {
        return fullReport;
    }

    public void setFullReport(String fullReport) {
        this.fullReport = fullReport;
        notifyPropertyChanged(BR.fullReport);
    }

    @Bindable
    public List getWorkoutLists() { return workoutLists; }

    public void setWorkoutLists(ArrayList workoutLists) {
        this.workoutLists = workoutLists;
        notifyPropertyChanged(BR.workoutLists);
    }
}