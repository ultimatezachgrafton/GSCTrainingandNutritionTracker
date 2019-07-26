package zachg.gsctrainingandnutritiontracker.Models;

import android.widget.EditText;

// Report is the class that will comprise the individual reports from clients

public class Report {

    private String mReportId; // unique ID
    private String mDate;
//    private Date mDate;
    private String mDateString; // date of report
    private String mClientName; // client's name
    private String mWeight; // client's weight

    private String mExerciseName;
    private int mRepsAmount;
    private int mWeightAmount;

    private EditText etCommentBox;
    // photos to include with questions
    private boolean mIsNew;

    // This constructor generates a universally unique ID, then initializes it as well as the Date field
    public Report() {
        mReportId = getId();
    }

    public Report(String id) {
        mReportId = id;
        mDate = new String();
        mDateString = mDate.toString();
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

    public String getClientName() { return mClientName; }

    public void setClientName(String client) { mClientName = client; }

    public String getWeight() { return mWeight; }

    public void setWeight(String weight) { this.mWeight = weight; }

    public boolean getIsNew() { return mIsNew; }

    public void setIsNew(boolean isNew) { this.mIsNew = isNew; }

    public String getPhotoFilename() { return "IMG_" + getId().toString() + ".jpg"; }

    public void setExerciseName(String exerciseName) { this.mExerciseName = exerciseName; }

    public String getExerciseName() { return mExerciseName; }

    public void setWeightAmount(int weightAmount) { this.mWeightAmount = weightAmount; }

    public int getWeightAmount() { return mWeightAmount; }

    public void setRepsAmount(int repsAmount) { this.mRepsAmount = repsAmount; }

    public int getRepsAmount(int repsAmount) { return mRepsAmount; }

}