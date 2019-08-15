package zachg.gsctrainingandnutritiontracker.ViewModels;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.Models.User;
import zachg.gsctrainingandnutritiontracker.Repositories.FirestoreRepository;

public class AdminListViewModel extends ViewModel {

    private MutableLiveData<ArrayList<User>> mUsers;
    private FirestoreRepository mRepo;
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();

    public void init() {
        if (mUsers != null) {
            return;
        }
        mRepo = FirestoreRepository.getInstance();
        mUsers = mRepo.getUsers();
    }

    public void addNewValue(final User user) {
        mIsUpdating.setValue(true);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                ArrayList<User> users = mUsers.getValue();
                users.add(user);
                mUsers.postValue(users);
                mIsUpdating.postValue(false);
            }

            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public LiveData<ArrayList<User>> getUsers() {
        return mUsers;
    }

    public LiveData<Boolean> getIsUpdating() {
        return mIsUpdating;
    }
}