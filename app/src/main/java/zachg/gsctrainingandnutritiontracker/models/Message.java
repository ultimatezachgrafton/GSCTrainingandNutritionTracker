package zachg.gsctrainingandnutritiontracker.models;

public class Message {

    public void Msg() {

    }

    private String id;
    private String title;
    private String body;
    private String date;
    private String clientName;
    private boolean isRead;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public boolean getIsRead() { return isRead; }

    public void setIsRead(boolean isRead) {this.isRead = isRead; }

}
