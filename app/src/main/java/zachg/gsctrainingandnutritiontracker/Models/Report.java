package zachg.gsctrainingandnutritiontracker.Models;

import android.widget.EditText;

import java.util.Date;

import static zachg.gsctrainingandnutritiontracker.UI.Fragments.AdminListFragment.currentSelectedUser;

// Report is the class that will comprise the individual reports from clients

public class Report {

    private String mReportId; // unique ID
    private Date mDate; // date of report
    private String mDateString; // date converted to string
    private String mClientName; // client's name
    private String mDailyWeight; // client's weight

    private String mExerciseName; // name of exercise
    private int mRepsAmount; // amount of sets and reps assigned to exercise
    private int mWeightAmount; // amount of weight in lbs used in each set

    private EditText etCommentBox; // space for exercise-related comments
    private String mComments;
    // photos to include with questions
    private boolean mIsNew; // determines if report is new

    // This constructor generates a universally unique ID, then initializes it as well as the Date field
    public Report() {
        mReportId = getId();
    }

    public Report(String id) {
        this.mReportId = id;
        this.mDate = new Date();
        this.mDateString = mDate.toString();
        this.mClientName = getClientName();
    }

    // Setters and getters
    public String getId() {
            return mReportId;
        }

    public String getDate() {
            return mDateString;
        }

    public void setDate(String date) {
            mDateString = date;
        }

    public String getClientName() {
        mClientName = currentSelectedUser.getClientName();
        return mClientName; }

    public void setClientName(String client) { mClientName = client; }

    public String getDailyWeight() { return mDailyWeight; }

    public void setWeight(String weight) { this.mDailyWeight = weight; }

    public boolean getIsNew() { return mIsNew; }

    public void setIsNew(boolean isNew) { this.mIsNew = isNew; }

//    public String getPhotoFilename() { return "IMG_" + getId().toString() + ".jpg"; }

    public void setExerciseName(String exerciseName) { this.mExerciseName = exerciseName; }

    public String getExerciseName() { return mExerciseName; }

    public void setWeightAmount(int weightAmount) { this.mWeightAmount = weightAmount; }

    public int getWeightAmount() { return mWeightAmount; }

    public void setReps(int repsAmount) { this.mRepsAmount = repsAmount; }

    public int getReps() { return mRepsAmount; }

    public String getComments() { return mComments; }

}