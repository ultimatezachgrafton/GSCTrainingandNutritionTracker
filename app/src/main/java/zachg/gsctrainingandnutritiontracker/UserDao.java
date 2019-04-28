package zachg.gsctrainingandnutritiontracker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Query("DELETE FROM users")
    void deleteAll();

    @Query("SELECT * from users ORDER BY user ASC")
    LiveData<List<User>> getAllUsers();
}