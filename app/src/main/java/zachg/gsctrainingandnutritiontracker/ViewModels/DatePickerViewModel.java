package zachg.gsctrainingandnutritiontracker.ViewModels;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import zachg.gsctrainingandnutritiontracker.Models.Report;
import zachg.gsctrainingandnutritiontracker.Repositories.FirestoreRepository;

public class DatePickerViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Report>> mReports;
    private FirestoreRepository mRepo;
    private MutableLiveData<Boolean> mIsUpdating = new MutableLiveData<>();

    public void init() {
        if (mReports != null) {
            return;
        }

        mRepo = FirestoreRepository.getInstance();
        mReports = mRepo.getReports();
    }

    public void addNewValue(final Report report) {
        mIsUpdating.setValue(true);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                ArrayList<Report> reports = mReports.getValue();
                reports.add(report);
                mReports.postValue(reports);
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

    public LiveData<ArrayList<Report>> getReports() {
        return mReports;
    }

    public LiveData<Boolean> getIsUpdating() {
        return mIsUpdating;
    }
}
