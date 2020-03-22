package zachg.gsctrainingandnutritiontracker.models;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.FragmentActivity;

public class User extends BaseObservable {

    @NonNull
    private String id;
    @NonNull
    private String email;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String clientName;
    @NonNull
    private String password;
    private String confirmPassword;

    @NonNull
    private String phoneNumber;

    private boolean isAdmin;
    private boolean isLoggedIn;

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email) {
        this.email = email;
    }

    public User(String firstName, String lastName, String phoneNumber, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        setClientName(this.firstName, this.lastName);
        this.email = email;
        this.password = password;
        this.id = getId();
    }

    @Bindable
    public String getId() { return id; }

    public void setId(String id) { this.id = id;
    notifyPropertyChanged(BR.id);}

    @Bindable
    public String getEmail() { return email; }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setClientName(String firstName, String lastName) {
        concatClientName(firstName, lastName);
        this.clientName = clientName;
    }

    public String getClientName() { return clientName; }

    public String concatClientName(String firstName, String lastName) {
        this.clientName = (firstName + " " + lastName);
        return clientName; }

    public boolean getIsLoggedIn() { return isLoggedIn; }

    public void setIsLoggedIn(boolean isLoggedIn) { this.isLoggedIn = false; }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        notifyPropertyChanged(BR.isAdmin);
    }

    @Bindable
    public boolean getIsAdmin() { return isAdmin; }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @NonNull
    @Bindable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NonNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
        notifyPropertyChanged(BR.phoneNumber);
    }

    public String getPhotoFilename() { return "IMG_" + getId() + ".jpg"; }
}