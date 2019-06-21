package zachg.gsctrainingandnutritiontracker.reports;

import java.util.Date;
import java.util.UUID;

// Report is the class that will comprise the individual reports from clients

public class Report {

    private UUID mId; // unique ID
    private Date mDate; // date of report
    private String mClientName; // client's name
    private String mWeight; // client's weight
    // box for questions for Ben
    // photos to include with questions
    private boolean mIsUnread = true;

    // This constructor generates a universally unique ID, then initializes it as well as the Date field
    public Report() {
            this(UUID.randomUUID());
        }
        public Report(UUID id) {
            mId = id;
            mDate = new Date();
            this.mClientName = getClientName();
        }

    // Setters and getters
    public UUID getId() {
            return mId;
        }

    public Date getDate() {
            return mDate;
        }

    public void setDate(Date date) {
            mDate = date;
        }

    public String getClientName() { return mClientName; }

    public void setClientName(String client) { mClientName = client; }

    public String getWeight() { return mWeight; }

    public void setWeight(String weight) { this.mWeight = weight; }

    public boolean getIsUnread() { return mIsUnread; }

    public void setIsUnread(boolean isUnread) { this.mIsUnread = isUnread; }

    public String getPhotoFilename() { return "IMG_" + getId().toString() + ".jpg"; }

}