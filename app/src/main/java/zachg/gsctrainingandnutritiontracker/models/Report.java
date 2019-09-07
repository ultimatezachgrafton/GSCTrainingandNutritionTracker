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

    private String comments;
    // photos to include with questions
    private boolean isNew; // determines if report is new

    public Report() {}

    public Report(User user, Date date) {
        this.date = date;
        this.clientName = user.getClientName();
    }

    public Report(String clientName, String date, String dailyWeight, String comments) {
        this.clientName = clientName;
        this.dateString = date;
        this.dailyWeight = dailyWeight;
        this.comments = comments;
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

    public void setDailyWeight(String weight) { this.dailyWeight = weight; }

    public boolean getIsNew() { return isNew; }

    public void setIsNew(boolean isNew) { this.isNew = isNew; }

//    public String getPhotoFilename() { return "IMG_" + getId().toString() + ".jpg"; }

    public String getComments() { return comments; }

}