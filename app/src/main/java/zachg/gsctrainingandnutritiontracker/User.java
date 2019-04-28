package zachg.gsctrainingandnutritiontracker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")

public class User {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user")
    private String mUser;

    public User(String user) {this.mUser = user;}

    public String getUser(){return this.mUser;}
}