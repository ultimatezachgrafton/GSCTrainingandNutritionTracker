package zachg.gsctrainingandnutritiontracker.viewmodels;

import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class DatePickerViewModel extends ViewModel {

    private MutableLiveData<FirestoreRecyclerOptions<Report>> mReports = new MutableLiveData<>();
    private FirestoreRepository mRepo;

    public void init(User user) {
        if (mReports != null) {
            return;
        }
        mRepo = FirestoreRepository.getInstance();
        mReports.setValue(mRepo.getReportsFromRepo(user));
    }
}
