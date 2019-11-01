package zachg.gsctrainingandnutritiontracker.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import zachg.gsctrainingandnutritiontracker.models.Report;
import zachg.gsctrainingandnutritiontracker.models.User;
import zachg.gsctrainingandnutritiontracker.repositories.FirestoreRepository;

public class ClientProfileViewModel extends ViewModel {

    private MutableLiveData<FirestoreRecyclerOptions<Report>> reports = new MutableLiveData<>();
    private FirestoreRepository repo;
    private User currentUser = new User();

    public ClientProfileViewModel() {

    }

    public ClientProfileViewModel(User user) {
        this.currentUser = user;
        this.currentUser.setClientName(user.getClientName());
    }

    public void init(User user) {
        if (reports != null) {
            return;
        }
        repo = FirestoreRepository.getInstance();
        reports.setValue(repo.getReportsByUser(user));
    }
}
