package zachg.gsctrainingandnutritiontracker;

import androidx.annotation.NonNull;

public class User {

    @NonNull
    private int id;
    @NonNull
    private String email;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    private String birthday;
    @NonNull
    private String password;
    private String gender;
    private boolean isAdmin;

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.id = getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean getIsLoggedIn(boolean isLoggedIn) { return isLoggedIn; }

    public void setIsLoggedIn(boolean isLoggedIn) { isLoggedIn = false; }

    public boolean setIsAdmin(boolean isAdmin) { return false; }

    public void getIsAdmin(boolean isAdmin) { this.isAdmin = false; }

}
