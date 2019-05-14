package zachg.gsctrainingandnutritiontracker;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {

    @Insert
    void addUser(User user);

    //@Insert
    //void createAdmin(Admin admin);

    @Query("SELECT * FROM users")
    List<User> getUser();

    @Query("SELECT * FROM users ORDER BY clientName")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM users WHERE id = :id")
    List<User> getUserById(int id);

    @Query("SELECT * FROM users WHERE clientName = :clientName LIMIT 1")
    List<User> getUserByName(String clientName);

    @Delete
    void DeleteUser(User user);

    @Query("DELETE FROM users")
    void deleteAll();
}
