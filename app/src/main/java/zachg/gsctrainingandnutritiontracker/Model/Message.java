package zachg.gsctrainingandnutritiontracker.Model;

public class Message {

    public void Msg() {

    }

    private String msgId;
    private String msgTitle;
    private String msgBody;
    private String msgDate;
    private String clientName;
    private boolean isRead;

    public String getId() {
        return msgId;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
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
