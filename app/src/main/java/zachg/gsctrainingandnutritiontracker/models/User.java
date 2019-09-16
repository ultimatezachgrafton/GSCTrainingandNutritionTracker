package zachg.gsctrainingandnutritiontracker.models;

import androidx.annotation.NonNull;

public class User {

    @NonNull
    private String id;
    @NonNull
    private String email;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    private String clientName;
    private String birthday;
    @NonNull
    private String password;
    private String gender;
    private String isAdminString;
    private boolean isAdmin;
    private boolean isLoggedIn;

    private String dateJoined;
    private int workoutNum = 1; // tracks which workout day the user is on
    private int day = 1;

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email) {
        this.email = email;
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.clientName = getClientName();
        this.email = email;
        this.password = password;
        this.id = getId();
        this.workoutNum = getWorkoutNum();
        this.day = getDay();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getBirthdate() {
        return birthday;
    }

    public void setBirthdate(String birthday) {
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

    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getClientName() { return clientName; }

    public String concatClientName() {
        this.clientName = (this.firstName + " " + this.lastName);
        return clientName; }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean getIsLoggedIn() { return isLoggedIn; }

    public void setIsLoggedIn(boolean isLoggedIn) { this.isLoggedIn = false; }

    public void setIsAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }

    public boolean getIsAdmin() { return isAdmin; }

    public String getIsAdminString() { return isAdminString; }

    public void setIsAdminString(boolean isAdmin) {
        if (isAdmin) { this.isAdminString = "Admin"; } else { isAdminString = "User"; }
    }

    public void setDateJoined(String dateJoined) { this.dateJoined = dateJoined; }

    public String getDateJoined() { return dateJoined; }

    public void setWorkoutNum(int workoutNum) {
        this.workoutNum = workoutNum;
    }

    public int getWorkoutNum() {
        return workoutNum;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int workoutDay) {
        this.day = day;
    }

    public void incrementWorkout() { workoutNum = workoutNum++; }

    public void incrementDay() { day = day++; }
}