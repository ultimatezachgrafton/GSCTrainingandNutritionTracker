package zachg.gsctrainingandnutritiontracker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @PrimaryKey (autoGenerate = true)
    @NonNull
    private int id;
    @ColumnInfo(name = "clientName")
    private String clientName;
    @ColumnInfo(name = "birthday")
    private String birthday;
    @ColumnInfo(name = "password")
    private String password;
    @ColumnInfo(name = "gender")
    private String gender;

    public User() {}

    public User(String user) {
        this.clientName = user;
        this.password = null;
        this.id = getId(); }

    public User(String clientName, String password) {
        this.clientName = clientName;
        this.password = password;
        this.id = getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean getIsLoggedIn(boolean isLoggedIn) { return isLoggedIn; }

    public void setIsLoggedIn(boolean isLoggedIn) { isLoggedIn = false; }

    public String getUserName() { return this.getClientName(); }

}
